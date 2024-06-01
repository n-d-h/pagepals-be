package com.pagepal.capstone.utils;

import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.dtos.recording.RecordingDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.entities.postgre.Record;
import com.pagepal.capstone.enums.*;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.WebhookService;
import com.pagepal.capstone.services.ZoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final WebhookService webhookService;

    private final DateUtils dateUtils;
    private final BookingRepository bookingRepository;
    private final ZoomService zoomService;
    private final RecordRepository recordRepository;
    private final RecordFileRepository recordFileRepository;
    private final BookingStateRepository bookingStateRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final SettingRepository settingRepository;
    private final EventRepository eventRepository;

    @Scheduled(fixedRate = 1800000)
    public void scheduleTaskWithFixedRate() {

        Date currentTime = dateUtils.getCurrentVietnamDate();
        Date startTime = new Date(currentTime.getTime() - 3 * 60 * 60 * 1000);

        List<Booking> bookings = bookingRepository.findByStartAtBetween(startTime, currentTime);

        int count = 0;

        for(var booking : bookings){
            Date endTime;
            if (booking.getService() != null) {
                endTime = new Date(booking.getStartAt().getTime() + 60 * 60 * 1000);
            } else {
                Integer duration = booking.getEvent().getSeminar().getDuration();
                endTime = new Date(booking.getStartAt().getTime() + duration * 60 * 1000);
            }

            long timeDifferenceMillis = currentTime.getTime() - endTime.getTime();

            if (timeDifferenceMillis  >= 0 && timeDifferenceMillis <= 3600000) {
                if (updateBookingRecord(booking)) {
                    count++;
                }
            }
        }

        // Your task logic goes here
        Map<String, String> content = new HashMap<>();
        content.put("Content", "Schedule task executed");
        content.put("Job", "Update booking record");
        content.put("Task executed at", dateUtils.getCurrentVietnamDate().toString());
        content.put("Number of booking start in last 3 hours", String.valueOf(bookings.size()));
        content.put("Number of booking updated", String.valueOf(count));
        webhookService.sendWebhookWithDataSchedule("UPDATE BOOKING RECORD", content, Boolean.FALSE);
    }

    @Scheduled(cron = "59 55 23 * * ?")
//    @Scheduled(fixedRate = 1800000)
    public void scheduleTaskToUpdateBookingState() {

        Setting revenue = settingRepository.findByKey("REVENUE_SHARE").orElse(null);
        Setting tokenPrice = settingRepository.findByKey("TOKEN_PRICE").orElse(null);

        if (revenue == null || tokenPrice == null) {
            throw new EntityNotFoundException("Setting not found");
        }

        Date currentTime = dateUtils.getCurrentVietnamDate();

        Date startTime = new Date(currentTime.getTime() - (24 * 3600 * 1000) - 3600000);
        Date endTime = new Date(currentTime.getTime() - 3600000);

        BookingState completeState = bookingStateRepository.findByName("COMPLETE").orElseThrow(() -> new EntityNotFoundException("Booking state not found"));

        BookingState pendingState = bookingStateRepository.findByName("PENDING").orElseThrow(() -> new EntityNotFoundException("Booking state not found"));

        List<Booking> serviceBookings = bookingRepository.findByStartAtBetweenAndStatePending(startTime, endTime);

        for (var booking : serviceBookings) {
            List<Record> records = recordRepository.findByMeetingCode(booking.getMeeting().getMeetingCode());
            if(records == null || records.isEmpty()){
                refundForCustomer(booking);
            }else{
                int count = 0;
                for (var record : records) {
                    count += record.getDuration();
                }
                if(count < 40){
                    refundForCustomer(booking);
                }else {
                    booking.setState(completeState);
                    booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
                    bookingRepository.save(booking);

                    Float receiveCash = ((booking.getTotalPrice() * Float.parseFloat(tokenPrice.getValue()))
                            * (100 - Float.parseFloat(revenue.getValue()))) / 100;
                    Wallet wallet = booking.getService().getReader().getAccount().getWallet();
                    wallet.setCash(wallet.getCash() + receiveCash);
                    wallet = walletRepository.save(wallet);

                    Transaction transaction = new Transaction();
                    transaction.setStatus(TransactionStatusEnum.SUCCESS);
                    transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
                    transaction.setTransactionType(TransactionTypeEnum.BOOKING_DONE_RECEIVE);
                    transaction.setCurrency(CurrencyEnum.DOLLAR);
                    transaction.setBooking(booking);
                    transaction.setAmount(Double.valueOf(receiveCash));
                    transaction.setWallet(wallet);
                    transactionRepository.save(transaction);
                }
            }
        }

        List<Event> eventBookings = eventRepository.findByStartAtBetweenAndState(startTime, endTime, EventStateEnum.ACTIVE);

        for (var event : eventBookings) {
            List<Booking> bookings = bookingRepository.findByEventAndState(event, pendingState);
            if (bookings == null || bookings.isEmpty()) {
                continue;
            }
            List<Record> records = recordRepository.findByMeetingCode(bookings.get(0).getMeeting().getMeetingCode());
            if (records == null || records.isEmpty()) {
                for (var booking : bookings) {
                    refundForCustomer(booking);
                }
            }else{
                int count = 0;
                for (var record : records) {
                    count += record.getDuration();
                }
                if(count < event.getSeminar().getDuration() * 0.8){
                    for (var booking : bookings) {
                        refundForCustomer(booking);
                    }
                }else {
                    int totalPrice = event.getPrice() * bookings.size();
                    Float receiveCash = ((totalPrice * Float.parseFloat(tokenPrice.getValue()))
                            * (100 - Float.parseFloat(revenue.getValue()))) / 100;
                    Wallet wallet = event.getSeminar().getReader().getAccount().getWallet();
                    wallet.setCash(wallet.getCash() + receiveCash);
                    wallet = walletRepository.save(wallet);

                    Transaction transaction = new Transaction();
                    transaction.setStatus(TransactionStatusEnum.SUCCESS);
                    transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
                    transaction.setTransactionType(TransactionTypeEnum.BOOKING_DONE_RECEIVE);
                    transaction.setCurrency(CurrencyEnum.DOLLAR);
                    transaction.setAmount(Double.valueOf(receiveCash));
                    transaction.setWallet(wallet);
                    transactionRepository.save(transaction);

                    for (var booking : bookings) {
                        booking.setState(completeState);
                        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
                        bookingRepository.save(booking);
                    }
                }
            }
        }

        // Your task logic goes here
        Map<String, String> content = new HashMap<>();
        content.put("Content", "Schedule task executed");
        content.put("Job", "Update booking state");
        content.put("Task executed at", dateUtils.getCurrentVietnamDate().toString());
        webhookService.sendWebhookWithDataSchedule("UPDATE BOOKING STATE", content, Boolean.FALSE);
    }

    public void refundForCustomer(Booking booking) {
        booking.setState(bookingStateRepository.findByName("CANCEL").orElseThrow(() -> new EntityNotFoundException("Booking state not found")));
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
        booking.setCancelReason("Meeting does not meet the conditions for completion or does not take place");
        bookingRepository.save(booking);

        Wallet wallet = booking.getCustomer().getAccount().getWallet();
        wallet.setTokenAmount(wallet.getTokenAmount() + booking.getTotalPrice());
        wallet.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setAmount((double) booking.getTotalPrice());
        transaction.setTransactionType(TransactionTypeEnum.BOOKING_REFUND);
        transaction.setWallet(wallet);
        transaction.setCurrency(CurrencyEnum.TOKEN);
        transaction.setStatus(TransactionStatusEnum.SUCCESS);
        transaction.setBooking(booking);
        transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
        transactionRepository.save(transaction);
    }

    public boolean updateBookingRecord(Booking booking) {

        boolean isUpdated = false;

        Meeting meeting = booking.getMeeting();

        MeetingRecordings recording = zoomService.getListRecordingByMeetingId(booking.getMeeting().getMeetingCode());

        List<RecordingDto> recordingList = recording.getMeetings();

        if (recordingList == null || recordingList.isEmpty()) {
            return false;
        }

        List<Record> listRecord = new ArrayList<>();

        for (var recordDto : recordingList) {
            Record record = recordRepository.findByExternalId(recordDto.getUuid()).orElse(new Record());

            record.setRecordingCount(recordDto.getRecording_count());
            record.setStatus(Status.ACTIVE);
            record.setDuration(recordDto.getDuration());
            record.setExternalId(recordDto.getUuid());
            record.setStartTime(recordDto.getStart_time());
            record.setMeeting(meeting);

            record = recordRepository.save(record);

            List<RecordFile> recordFiles = record.getRecordFiles();
            if (recordFiles != null && !recordFiles.isEmpty()) {
                recordFileRepository.deleteAll(recordFiles);
            }

            Record finalRecord = record;
            List<RecordFile> listRecordFile = recordDto.getRecording_files().stream().map(file -> {
                return RecordFile
                        .builder()
                        .downloadUrl(file.getDownload_url())
                        .playUrl(file.getPlay_url())
                        .fileExtention(file.getFile_extension())
                        .fileType(file.getFile_type())
                        .startAt(file.getRecording_start())
                        .endAt(file.getRecording_end())
                        .recordingType(file.getRecording_type())
                        .status(Status.ACTIVE)
                        .record(finalRecord)
                        .build();
            }).toList();

            listRecordFile = recordFileRepository.saveAll(listRecordFile);
            record.setRecordFiles(listRecordFile);
            listRecord.add(record);
            isUpdated = true;
        }

        return isUpdated;
    }
}