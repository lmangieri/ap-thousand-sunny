delimiter //
CREATE PROCEDURE consolidatePoll()
BEGIN
	-- Primeiro passo, setar para 0 os votos consolidados, pois pode ser que o administrador reabriu a votação
    UPDATE poll_option T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id) SET T1.total_number_of_votes = 0;
	UPDATE poll_option T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id) SET T1.number_of_votes_with_ip_filter = 0;


	DROP TEMPORARY TABLE IF EXISTS TOTAL_VOTES;
	CREATE TEMPORARY TABLE TOTAL_VOTES AS select count(*) as total, option_number,T1.poll_id as poll_id from vote T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id)  group by option_number;

	DROP TEMPORARY TABLE IF EXISTS IP_DETAILS_TOTAL_VOTES;
	CREATE TEMPORARY TABLE IP_DETAILS_TOTAL_VOTES AS select count(*) as total, ip_details, T1.poll_id from vote T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id)  group by ip_details, T1.poll_id;

	-- Realizando update em VOTE para marcar ignored_by_ip_rule
	UPDATE vote T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id) INNER JOIN IP_DETAILS_TOTAL_VOTES T3 on (T1.poll_id = T3.poll_id and T1.ip_details = T3.ip_details) SET T1.ignored_by_ip_rule = true where T3.total > 1;

	DROP TEMPORARY TABLE IF EXISTS TOTAL_VOTES_WITHOUT_IGNORED;
	CREATE TEMPORARY TABLE TOTAL_VOTES_WITHOUT_IGNORED AS select count(*) as total, option_number,T1.poll_id as poll_id from vote T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id) where T1.ignored_by_ip_rule = FALSE group by option_number;


	-- Realizando update totalNumberOfVotes
	UPDATE poll_option T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id) INNER JOIN TOTAL_VOTES T3 on (T1.option_number = T3.option_number and T1.poll_id = T3.poll_id) SET T1.total_number_of_votes = T3.total;

	-- Realizando update numberOfVotesWithIpFilter
	UPDATE poll_option T1 INNER JOIN poll_control T2 on  (T1.poll_id = T2.poll_id) INNER JOIN TOTAL_VOTES_WITHOUT_IGNORED T3 on (T1.option_number = T3.option_number and T1.poll_id = T3.poll_id) SET T1.number_of_votes_with_ip_filter = T3.total;

END//

commit

--
DROP PROCEDURE IF EXISTS consolidatePoll;

CALL consolidatePoll()

select version()


