USE wcsDB;

/*  DELETE USER TABLE   */
DELETE FROM user
WHERE del_flag=1;

/* PAGE_OPTION */
DELETE speed,o,r
FROM page_option o
INNER JOIN page_option_page r on o.id = r.page_option_id
INNER JOIN speed_test_report speed on o.id = speed.page_option_id
WHERE o.del_flag = 1;

/* REPORT */

/* SPEED TEST */
DELETE FROM speed_test_report
WHERE del_flag=1;


