CREATE TABLE IF NOT EXISTS users (
                       user_id        UUID PRIMARY KEY,
                       tenant_id      UUID NOT NULL,
                       email          VARCHAR(255) NOT NULL UNIQUE,
                       password       VARCHAR(255) NOT NULL,
                       role           VARCHAR(50) NOT NULL, -- admin, vendor, etc
                       created_at     TIMESTAMP DEFAULT NOW(),
                       updated_at     TIMESTAMP DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS user_sessions (
                               session_id   UUID PRIMARY KEY,
                               user_id      UUID REFERENCES users(user_id),
                               tenant_id    UUID NOT NULL,
                               refresh_token TEXT NOT NULL,
                               expires_at   TIMESTAMP NOT NULL,
                               created_at   TIMESTAMP DEFAULT NOW()
);

INSERT INTO users (
    user_id,
    tenant_id,
    email,
    password,
    role
) VALUES
      (
          '180f1d0a-522d-4194-ac5e-51909104975b',
          '8d3457d3-1841-4750-9b3b-9351a789ab26',
          'admin@system.com',
          '$2a$12$ergAFHIkMrm0xoSA1d52C.fV3Yf05EZQeMURETmOxAX4I89Uqs6zC',
          'ADMIN'
--           '2025-01-01 10:00:00',
--           '2025-01-01 10:00:00'
      ),
      (
          'b8dec86a-7a1e-4acf-92c7-88eb22e5214d',
          'd76ffb0a-26c3-409f-bc35-265c5cf5f9cc',
          'juan@example.com',
          '$2a$12$ergAFHIkMrm0xoSA1d52C.fV3Yf05EZQeMURETmOxAX4I89Uqs6zC',
          'CUSTOMER'
--           '2025-01-02 11:20:00',
--           '2025-01-02 11:20:00'
      ),
      (
          '2dcf8164-3138-43d1-a061-6a8c0c81da5c',
          '03bdb194-1c64-4259-ad14-c61b80ea0865',
          'empresa1@example.com',
          '$2a$12$ergAFHIkMrm0xoSA1d52C.fV3Yf05EZQeMURETmOxAX4I89Uqs6zC',
          'COMPANY'
--           '2025-01-03 09:15:00',
--           '2025-01-03 09:15:00'
      );


INSERT INTO user_sessions (
    session_id,
    user_id,
    tenant_id,
    refresh_token,
    expires_at
) VALUES
      (
          '7f71497b-010e-4a4f-be3c-52562fdeba8a',
          'b8dec86a-7a1e-4acf-92c7-88eb22e5214d',
          'd76ffb0a-26c3-409f-bc35-265c5cf5f9cc',
          'REFRESH_tok_cliente_01',
          --'2025-01-10 08:30:00',
          '2025-01-11 08:30:00'
      ),
      (
          '0b2e9532-040d-4d72-8183-342e026c39b7',
          '2dcf8164-3138-43d1-a061-6a8c0c81da5c',
          '03bdb194-1c64-4259-ad14-c61b80ea0865',
          'REFRESH_tok_empresa_01',
          --'2025-01-09 10:00:00',
          '2025-01-10 10:00:00'
      );

