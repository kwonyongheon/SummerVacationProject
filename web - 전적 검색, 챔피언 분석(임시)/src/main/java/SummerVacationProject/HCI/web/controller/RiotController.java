package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.dto.Participant;
import SummerVacationProject.HCI.web.dto.PlayerMatch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RiotController {

    private static final Logger logger = LoggerFactory.getLogger(RiotController.class);
    private final String RIOT_API_KEY = "RGAPI-bec8f2ef-3bf4-4ebb-9dcc-79f956ca19ad";
    private Map<Integer, String> spellData;
    private Map<Integer, String> itemData;

    public RiotController() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        URL spellUrl = new URL("https://ddragon.leagueoflegends.com/cdn/14.16.1/data/ko_KR/summoner.json");
        Map<String, Object> spellMap = objectMapper.readValue(spellUrl, new TypeReference<Map<String, Object>>() {});
        this.spellData = ((Map<String, Map<String, Object>>) spellMap.get("data")).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Integer.parseInt((String) e.getValue().get("key")),
                        e -> (String) e.getValue().get("id")
                ));

        URL itemUrl = new URL("https://ddragon.leagueoflegends.com/cdn/14.16.1/data/ko_KR/item.json");
        Map<String, Object> itemMap = objectMapper.readValue(itemUrl, new TypeReference<Map<String, Object>>() {});
        this.itemData = ((Map<String, Map<String, Object>>) itemMap.get("data")).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Integer.parseInt(e.getKey()),
                        e -> (String) e.getValue().get("name")
                ));
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String gameName, @RequestParam String tagLine, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            logger.info("Account API 호출 시작 - 소환사 이름: {}, 태그: {}", gameName, tagLine);

            String accountApiUrl = UriComponentsBuilder.fromHttpUrl("https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
                    .queryParam("api_key", RIOT_API_KEY)
                    .buildAndExpand(gameName, tagLine)
                    .toUriString();

            Map<String, Object> accountData = restTemplate.getForObject(accountApiUrl, Map.class);
            logger.info("Account API 응답: {}", accountData);

            if (accountData == null || accountData.isEmpty()) {
                logger.error("계정 정보를 가져올 수 없습니다.");
                model.addAttribute("error", "계정 정보를 가져올 수 없습니다. 올바른 플레이어 이름과 태그를 확인하세요.");
                return "result";
            }

            String puuid = (String) accountData.get("puuid");
            String fullSummonerName = gameName + " #" + tagLine;

            String summonerApiUrl = UriComponentsBuilder.fromHttpUrl("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/{puuid}")
                    .queryParam("api_key", RIOT_API_KEY)
                    .buildAndExpand(puuid)
                    .toUriString();

            Map<String, Object> summonerData = restTemplate.getForObject(summonerApiUrl, Map.class);
            logger.info("Summoner API 응답: {}", summonerData);

            if (summonerData == null || summonerData.isEmpty()) {
                logger.error("소환사 정보를 가져올 수 없습니다.");
                model.addAttribute("error", "소환사 정보를 가져올 수 없습니다.");
                return "result";
            }

            String profileIconId = summonerData.get("profileIconId").toString();
            String profileIconUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/profileicon/" + profileIconId + ".png";

            String rankApiUrl = UriComponentsBuilder.fromHttpUrl("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{id}")
                    .queryParam("api_key", RIOT_API_KEY)
                    .buildAndExpand(summonerData.get("id"))
                    .toUriString();

            List<Map<String, Object>> rankData = restTemplate.getForObject(rankApiUrl, List.class);
            logger.info("Rank API 응답: {}", rankData);

            String soloRank = "Unranked";
            String flexRank = "Unranked";

            for (Map<String, Object> rankInfo : rankData) {
                if ("RANKED_SOLO_5x5".equals(rankInfo.get("queueType"))) {
                    soloRank = rankInfo.get("tier") + " " + rankInfo.get("rank") + " (" + rankInfo.get("leaguePoints") + " LP)";
                } else if ("RANKED_FLEX_SR".equals(rankInfo.get("queueType"))) {
                    flexRank = rankInfo.get("tier") + " " + rankInfo.get("rank") + " (" + rankInfo.get("leaguePoints") + " LP)";
                }
            }

            String matchListApiUrl = UriComponentsBuilder.fromHttpUrl("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids")
                    .queryParam("start", 0)
                    .queryParam("count", 5)
                    .queryParam("api_key", RIOT_API_KEY)
                    .buildAndExpand(puuid)
                    .toUriString();

            List<String> matchIds = restTemplate.getForObject(matchListApiUrl, List.class);
            logger.info("Match History API 응답: {}", matchIds);

            if (matchIds == null || matchIds.isEmpty()) {
                logger.warn("최근 경기 기록이 없습니다.");
                model.addAttribute("error", "최근 경기 기록이 없습니다.");
                return "result";
            }

            List<PlayerMatch> matches = new ArrayList<>();

            for (String matchId : matchIds) {
                String matchDetailApiUrl = UriComponentsBuilder.fromHttpUrl("https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}")
                        .queryParam("api_key", RIOT_API_KEY)
                        .buildAndExpand(matchId)
                        .toUriString();

                Map<String, Object> matchDetail = restTemplate.getForObject(matchDetailApiUrl, Map.class);
                logger.info("Match Detail API 응답: {}", matchDetail);

                Map<String, Object> info = (Map<String, Object>) matchDetail.get("info");
                List<Map<String, Object>> participants = (List<Map<String, Object>>) info.get("participants");

                PlayerMatch playerMatch = new PlayerMatch();
                playerMatch.setQueueType(info.get("queueId").toString());

                long daysAgo = ChronoUnit.DAYS.between(Instant.ofEpochMilli(((Number) info.get("gameStartTimestamp")).longValue()), Instant.now());
                playerMatch.setDaysAgo(daysAgo + "일 전");

                long gameDuration = ((Number) info.get("gameDuration")).longValue();
                playerMatch.setDuration((gameDuration / 60) + "분");

                List<Participant> allies = new ArrayList<>();
                List<Participant> enemies = new ArrayList<>();

                for (Map<String, Object> participant : participants) {
                    Participant p = new Participant();
                    p.setSummonerName((String) participant.get("summonerName"));
                    p.setChampionName((String) participant.get("championName"));
                    p.setKills(((Number) participant.get("kills")).intValue());
                    p.setDeaths(((Number) participant.get("deaths")).intValue());
                    p.setAssists(((Number) participant.get("assists")).intValue());
                    p.setChampLevel(((Number) participant.get("champLevel")).intValue());
                    p.setPuuid((String) participant.get("puuid"));

                    // 스펠 아이콘 URL 설정
                    String spell1Key = spellData.get(((Number) participant.get("summoner1Id")).intValue());
                    String spell2Key = spellData.get(((Number) participant.get("summoner2Id")).intValue());
                    p.setSpell1Id(spell1Key);
                    p.setSpell2Id(spell2Key);

                    // 아이템 아이콘 URL 설정
                    p.setItem0(participant.get("item0").toString());
                    p.setItem1(participant.get("item1").toString());
                    p.setItem2(participant.get("item2").toString());
                    p.setItem3(participant.get("item3").toString());
                    p.setItem4(participant.get("item4").toString());
                    p.setItem5(participant.get("item5").toString());
                    p.setItem6(participant.get("item6").toString());

                    // 챔피언 아이콘 URL 설정
                    String championIconUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/champion/" + p.getChampionName() + ".png";
                    p.setChampionIconUrl(championIconUrl);

                    if (puuid.equals(participant.get("puuid"))) {
                        playerMatch.setWin((Boolean) participant.get("win"));
                        playerMatch.setChampionName(p.getChampionName());
                        playerMatch.setChampionIconUrl(p.getChampionIconUrl());
                        playerMatch.setKills(p.getKills());
                        playerMatch.setDeaths(p.getDeaths());
                        playerMatch.setAssists(p.getAssists());
                    }

                    if (participant.get("teamId").equals(100)) {
                        allies.add(p);
                    } else {
                        enemies.add(p);
                    }
                }
                playerMatch.setAllies(allies);
                playerMatch.setEnemies(enemies);

                matches.add(playerMatch);
            }

            model.addAttribute("fullSummonerName", fullSummonerName);
            model.addAttribute("level", summonerData.get("summonerLevel"));
            model.addAttribute("profileIconUrl", profileIconUrl);
            model.addAttribute("soloRank", soloRank);
            model.addAttribute("flexRank", flexRank);
            model.addAttribute("matches", matches);
        } catch (HttpClientErrorException e) {
            logger.error("HTTP 오류 발생: 상태 코드: {}, 응답: {}", e.getStatusCode(), e.getResponseBodyAsString());
            model.addAttribute("error", "검색 중 오류가 발생했습니다. 상세 오류: " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("일반 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "검색 중 오류가 발생했습니다. 상세 오류: " + e.getMessage());
        }

        return "result";
    }
}
