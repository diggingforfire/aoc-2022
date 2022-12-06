--postgres 9.6
SELECT POSITION(substring('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw' FROM n FOR 4) in 'zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw') + 3
FROM generate_series(1, length('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw') - 3, 1) n
WHERE substring('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw' FROM n FOR 4)  ~ '^.*(.).*\1.*$' = false
LIMIT 1