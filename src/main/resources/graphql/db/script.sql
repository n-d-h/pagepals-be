INSERT INTO account_state (id, name, status)
VALUES (gen_random_uuid(), 'ACTIVE', 'ACTIVE'),
       (gen_random_uuid(), 'INACTIVE', 'ACTIVE'),
       (gen_random_uuid(), 'PENDING_VERIFICATION', 'ACTIVE'),
       (gen_random_uuid(), 'CLOSED', 'ACTIVE'),
       (gen_random_uuid(), 'BLOCKED', 'ACTIVE'),
       (gen_random_uuid(), 'LIMITED', 'ACTIVE'),
       (gen_random_uuid(), 'LOCKED', 'ACTIVE'),
       (gen_random_uuid(), 'EXPIRED', 'ACTIVE'),
       (gen_random_uuid(), 'PENDING_CANCELLATION', 'ACTIVE');