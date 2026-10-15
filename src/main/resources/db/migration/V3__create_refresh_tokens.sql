CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) UNIQUE NOT NULL,
    expiration_timestamp TIMESTAMP NOT NULL,
    revocation_timestamp TIMESTAMP,
    creation_timestamp TIMESTAMP NOT NULL,
    replaced_by UUID REFERENCES refresh_tokens(id)
);
CREATE INDEX idx_refresh_tokens_hash ON refresh_tokens(token_hash);
