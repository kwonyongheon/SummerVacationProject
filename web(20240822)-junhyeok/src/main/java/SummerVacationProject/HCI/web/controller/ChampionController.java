package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.dto.Champion;
import SummerVacationProject.HCI.web.dto.Skill;
import SummerVacationProject.HCI.web.dto.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ChampionController {

    private static final Logger logger = LoggerFactory.getLogger(ChampionController.class);
    private final String DATA_DRAGON_URL = "https://ddragon.leagueoflegends.com/cdn/14.16.1/data/ko_KR/champion.json";

    // 챔피언 목록을 가져와서 뷰에 전달하는 메서드
    @GetMapping("/champions")
    public String listChampions(Model model) {
        RestTemplate restTemplate = new RestTemplate();

        // API 호출하여 챔피언 데이터 가져오기
        ResponseEntity<Map> responseEntity = restTemplate.exchange(DATA_DRAGON_URL, HttpMethod.GET, null, Map.class);
        Map<String, Object> response = responseEntity.getBody();

        if (response == null) {
            logger.error("Failed to fetch champions data");
            return "error/500";
        }

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        List<Champion> champions = new ArrayList<>();

        // 챔피언 데이터 파싱 및 객체 생성
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Map<String, Object> championData = (Map<String, Object>) entry.getValue();

            Champion champion = new Champion();
            champion.setId((String) championData.get("id"));
            champion.setName((String) championData.get("name"));
            champion.setTitle((String) championData.get("title"));

            String imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/champion/" + champion.getId() + ".png";
            champion.setImageUrl(imageUrl);

            champions.add(champion);
        }

        // 모델에 챔피언 리스트를 추가하여 뷰에 전달
        model.addAttribute("champions", champions);
        return "champions";
    }

    // 특정 챔피언의 상세 정보를 가져와서 뷰에 전달하는 메서드
    @GetMapping("/champions/{id}")
    public String viewChampion(@PathVariable String id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String champDetailsUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/data/ko_KR/champion/" + id + ".json";

        ResponseEntity<Map> responseEntity;
        try {
            // API 호출하여 챔피언 상세 정보 가져오기
            responseEntity = restTemplate.exchange(champDetailsUrl, HttpMethod.GET, null, Map.class);
        } catch (Exception e) {
            logger.error("Error fetching champion details", e);
            return "error/500";
        }

        Map<String, Object> response = responseEntity.getBody();
        if (response == null) {
            logger.error("Failed to fetch champion details for ID: " + id);
            return "error/500";
        }

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        Map<String, Object> championData = (Map<String, Object>) data.get(id);

        if (championData == null) {
            logger.error("Champion data not found for ID: " + id);
            return "error/404";
        }

        // 챔피언 객체 생성 및 데이터 설정
        Champion champion = new Champion();
        champion.setId((String) championData.get("id"));
        champion.setName((String) championData.get("name"));
        champion.setTitle((String) championData.get("title"));
        champion.setLore((String) championData.get("lore"));

        // 챔피언 능력치 설정
        Map<String, Object> statsData = (Map<String, Object>) championData.get("stats");
        if (statsData != null) {
            Stats stats = new Stats();
            stats.setHp(getDouble(statsData.get("hp")));
            stats.setHpperlevel(getDouble(statsData.get("hpperlevel")));
            stats.setMp(getDouble(statsData.get("mp")));
            stats.setMpperlevel(getDouble(statsData.get("mpperlevel")));
            stats.setMovespeed(getDouble(statsData.get("movespeed")));
            stats.setArmor(getDouble(statsData.get("armor")));
            stats.setArmorperlevel(getDouble(statsData.get("armorperlevel")));
            stats.setSpellblock(getDouble(statsData.get("spellblock")));
            stats.setSpellblockperlevel(getDouble(statsData.get("spellblockperlevel")));
            stats.setAttackrange(getDouble(statsData.get("attackrange")));
            stats.setHpregen(getDouble(statsData.get("hpregen")));
            stats.setHpregenperlevel(getDouble(statsData.get("hpregenperlevel")));
            stats.setMpregen(getDouble(statsData.get("mpregen")));
            stats.setMpregenperlevel(getDouble(statsData.get("mpregenperlevel")));
            stats.setCrit(getDouble(statsData.get("crit")));
            stats.setCritperlevel(getDouble(statsData.get("critperlevel")));
            stats.setAttackdamage(getDouble(statsData.get("attackdamage")));
            stats.setAttackdamageperlevel(getDouble(statsData.get("attackdamageperlevel")));
            stats.setAttackspeed(getDouble(statsData.get("attackspeed")));
            stats.setAttackspeedperlevel(getDouble(statsData.get("attackspeedperlevel")));
            champion.setStats(stats);
        } else {
            logger.warn("Stats data is missing for champion ID: " + id);
        }

        // 스킬과 스킨 데이터 처리
        List<Map<String, Object>> spellsData = (List<Map<String, Object>>) championData.get("spells");
        List<Skill> skills = new ArrayList<>();
        if (spellsData != null) {
            for (Map<String, Object> spell : spellsData) {
                Skill skill = new Skill();
                skill.setName((String) spell.get("name"));
                skill.setDescription((String) spell.get("description"));
                String skillImageUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/spell/" + spell.get("id") + ".png";
                skill.setImageUrl(skillImageUrl);
                skills.add(skill);
            }
        }

        // 패시브 스킬 데이터 처리
        Map<String, Object> passiveData = (Map<String, Object>) championData.get("passive");
        if (passiveData != null) {
            Skill passive = new Skill();
            passive.setName((String) passiveData.get("name"));
            passive.setDescription((String) passiveData.get("description"));
            String passiveImageUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/passive/" + ((Map<String, Object>) passiveData.get("image")).get("full");
            passive.setImageUrl(passiveImageUrl);
            skills.add(0, passive);  // 패시브 스킬을 리스트의 첫 번째에 추가
        }

        champion.setSkills(skills);

        // 스킨 이미지 및 이름 설정
        List<Map<String, Object>> skinsData = (List<Map<String, Object>>) championData.get("skins");
        List<String> skinImages = new ArrayList<>();
        List<String> skinNames = new ArrayList<>();
        if (skinsData != null) {
            for (Map<String, Object> skin : skinsData) {
                String skinNum = skin.get("num").toString();
                String skinName = (String) skin.get("name");
                String skinImageUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + champion.getId() + "_" + skinNum + ".jpg";
                logger.info("Skin image URL: " + skinImageUrl);
                skinImages.add(skinImageUrl);
                skinNames.add(skinName); // 스킨 이름 추가
            }
        }
        champion.setSkins(skinImages);
        champion.setSkinNames(skinNames); // 스킨 이름 설정

        String imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/champion/" + champion.getId() + ".png";
        champion.setImageUrl(imageUrl);

        // 모델에 챔피언 데이터를 추가하여 뷰에 전달
        model.addAttribute("champion", champion);
        return "champion-detail";
    }

    // 숫자 데이터를 double로 변환하는 헬퍼 메서드
    private double getDouble(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return 0;
    }

}
