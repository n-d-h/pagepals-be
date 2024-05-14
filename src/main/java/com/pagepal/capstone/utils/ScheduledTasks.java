package com.pagepal.capstone.utils;

import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.dtos.recording.RecordingDto;
import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.entities.postgre.Record;
import com.pagepal.capstone.entities.postgre.RecordFile;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.BookingRepository;
import com.pagepal.capstone.repositories.RecordFileRepository;
import com.pagepal.capstone.repositories.RecordRepository;
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
            if(recordRepository.findByExternalId(recordDto.getUuid()).isPresent()){
                continue;
            }

            Record record = new Record();
            record.setRecordingCount(recordDto.getRecording_count());
            record.setStatus(Status.ACTIVE);
            record.setDuration(recordDto.getDuration());
            record.setExternalId(recordDto.getUuid());
            record.setStartTime(recordDto.getStart_time());
            record.setMeeting(meeting);

            record = recordRepository.save(record);

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