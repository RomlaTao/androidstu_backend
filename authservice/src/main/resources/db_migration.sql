-- Migration script to add isUserInfoInitialized column to users table
-- Execute this script on your AuthService database

-- Add isUserInfoInitialized column to users table
ALTER TABLE users 
ADD COLUMN is_user_info_initialized BOOLEAN NOT NULL DEFAULT FALSE;

-- For existing users, set to TRUE (assuming they have already completed setup)
-- Comment out the line below if you want existing users to go through setup again
UPDATE users SET is_user_info_initialized = TRUE WHERE id IS NOT NULL;

-- Add index for performance (optional)
-- CREATE INDEX idx_users_is_user_info_initialized ON users(is_user_info_initialized); 