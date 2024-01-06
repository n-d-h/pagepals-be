SELECT gen_random_uuid() AS generated_uuid
FROM generate_series(1, 100);

-- Account State
INSERT INTO account_state (id, name, status)
VALUES ('069853bd-7d75-42c8-bf20-ce1ff83f95f1', 'ACTIVE', 'ACTIVE'),
       ('f4415f78-b69d-47c8-b207-586e3179915f', 'INACTIVE', 'ACTIVE'),
       ('ef91d513-9f86-46c3-881d-5d0ecea3ebb1', 'PENDING_VERIFICATION', 'ACTIVE'),
       ('9c93e2ec-f69f-4fa3-b209-378db1fdf4be', 'CLOSED', 'ACTIVE'),
       ('a70f7cfd-ec5d-4b5c-9fd4-ec8a1f4db3c4', 'BLOCKED', 'ACTIVE'),
       ('67eed08f-9cd0-4d49-8bab-ce747521b4db', 'LIMITED', 'ACTIVE'),
       ('8e230346-1f0f-4740-9851-4d393d934c7e', 'LOCKED', 'ACTIVE'),
       ('7f70bec0-3c47-476a-a679-b67268d3a5cf', 'EXPIRED', 'ACTIVE'),
       ('73194085-6528-48d9-83b8-424019a73f29', 'PENDING_CANCELLATION', 'ACTIVE');

-- Role
INSERT INTO role (id, name, status)
VALUES ('72b2c84a-448f-4427-94c4-2b27d765f2e1', 'READER', 'ACTIVE'),
       ('a8819f86-355b-4370-9deb-b4bc62252c28', 'ADMIN', 'ACTIVE'),
       ('338df8eb-5c35-4c0e-a6e5-535aafa8e07e', 'USER', 'ACTIVE');

-- Account
insert into account (id, created_at, deleted_at, email, login_type, password, updated_at, username, account_state_id,
                     role_id)
values ('6a27d080-d861-43fe-bc2e-f5a2168feda7', now(), null, 'account@gmail.com', 'NORMAL', '123456',
        now(), 'account', '069853bd-7d75-42c8-bf20-ce1ff83f95f1', '72b2c84a-448f-4427-94c4-2b27d765f2e1'
       );

-- Level
insert into level (id, amount_request, description, name, status)
values ('e581d064-0820-4a66-9d7a-d5023e4a21ef', 0, 'Level 1', 'Level 1', 'ACTIVE');

-- Category
insert into category (id, description, name, status)
values ('b01dc67b-16c5-442c-9c3c-d19d2262b45e', 'Category 1', 'Category 1', 'ACTIVE');

-- Book
insert into book (id, author, created_at, edition, image_url, "language", long_title, overview, pages, publisher,
                  status, title, category_id)
values ('bc484dab-1dcb-4ed0-a9c0-7480ec7a06f3', 'Author 1', now(), '1',
        'https://s3.amazonaws.com/audiobooks-mp3/1.mp3', 'ENGLISH', 'Long Title 1', 'Overview 1',
        10, 'Publisher 1', 'ACTIVE', 'Title 1', 'b01dc67b-16c5-442c-9c3c-d19d2262b45e'
       );

-- Chapter
insert into chapter (id, chapter_number, pages, status, book_id)
values ('161155b4-9e57-4918-9d1f-0ff1f3877c03', 1, 10, 'ACTIVE', 'bc484dab-1dcb-4ed0-a9c0-7480ec7a06f3');

-- Reader
insert into reader (id, audio_description_url, country_accent, created_at, deleted_at, description, experience, genre,
                    introduction_video_url, "language", nickname, rating, status, tags, total_of_bookings,
                    total_of_reviews, updated_at, account_id, level_id)
values ('3f9a5ed9-f580-4f2d-813a-583b685c6898', 'https://s3.amazonaws.com/audiobooks-mp3/1.mp3', 'US', now(),
        null, 'Reader 1', 5, 'FICTION', 'https://s3.amazonaws.com/audiobooks-mp3/1.mp3',
        'ENGLISH', 'reader1', 4.5, 'ACTIVE', 'FICTION, ROMANCE', 0, 0,
        now(), '6a27d080-d861-43fe-bc2e-f5a2168feda7', 'e581d064-0820-4a66-9d7a-d5023e4a21ef'
       );

-- Service
insert into service (id, created_at, description, duration, price, rating, status, total_of_booking, total_of_review,
                     chapter_id, reader_id)
values ('cf0fe86a-1f84-4a91-925d-2b9f6133a369',now(),'Service 1', 60, 100, 4.5, 'ACTIVE', 0,0,
        '161155b4-9e57-4918-9d1f-0ff1f3877c03', '3f9a5ed9-f580-4f2d-813a-583b685c6898');

insert into customer (
    id, created_at, deleted_at, dob, full_name, gender, image_url, status,
    updated_at, account_id)
values ('7050ae5f-a421-462e-b382-4b760c478018', now(), null, '1990-01-01', 'FullName',
        'MALE', 'https://s3.amazonaws.com/audiobooks-mp3/1.mp3', 'ACTIVE',
        now(), '6a27d080-d861-43fe-bc2e-f5a2168feda7');