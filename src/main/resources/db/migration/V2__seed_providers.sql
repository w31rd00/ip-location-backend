-- FreeIp
INSERT INTO provider (name, base_url, validity_days, enabled)
VALUES ('FreeIPAPI', 'https://freeipapi.com/api/json/', 30, TRUE)
ON CONFLICT (name) DO NOTHING;
