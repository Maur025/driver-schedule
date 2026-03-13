UPDATE tokens
SET state = 'EXPIRED'
WHERE state IS NULL;