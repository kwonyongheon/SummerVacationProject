package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.dto.Champion;
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

    private final String DATA_DRAGON_URL = "https://ddragon.leagueoflegends.com/cdn/14.16.1/data/en_US/champion.json";

    @GetMapping("/champions")
    public String listChampions(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(DATA_DRAGON_URL, Map.class);

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        List<Champion> champions = new ArrayList<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Map<String, Object> championData = (Map<String, Object>) entry.getValue();

            Champion champion = new Champion();
            champion.setId((String) championData.get("id"));
            champion.setName((String) championData.get("name"));
            champion.setTitle((String) championData.get("title"));

            // 이미지 URL 생성
            String imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/champion/" + champion.getId() + ".png";
            champion.setImageUrl(imageUrl);

            champions.add(champion);
        }

        model.addAttribute("champions", champions);
        return "champions";
    }

    @GetMapping("/champions/{id}")
    public String viewChampion(@PathVariable String id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String champDetailsUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/data/en_US/champion/" + id + ".json";
        Map<String, Object> response = restTemplate.getForObject(champDetailsUrl, Map.class);

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        Map<String, Object> championData = (Map<String, Object>) data.get(id);

        if (championData == null) {
            return "error/404";
        }

        Champion champion = new Champion();
        champion.setId((String) championData.get("id"));
        champion.setName((String) championData.get("name"));
        champion.setTitle((String) championData.get("title"));
        champion.setLore((String) championData.get("lore")); // 챔피언의 특징 설명 추가

        // 스킨 정보 추가
        List<Map<String, Object>> skinsData = (List<Map<String, Object>>) championData.get("skins");
        List<String> skins = new ArrayList<>();
        for (Map<String, Object> skin : skinsData) {
            String skinName = (String) skin.get("name");
            String skinNum = skin.get("num").toString();
            String skinImageUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + champion.getId() + "_" + skinNum + ".jpg";
            skins.add(skinName + "|" + skinImageUrl); // 스킨 이름과 이미지 URL을 합쳐서 저장
        }
        champion.setSkins(skins);

        // 이미지 URL 생성
        String imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.16.1/img/champion/" + champion.getId() + ".png";
        champion.setImageUrl(imageUrl);

        model.addAttribute("champion", champion);
        return "champion-detail";
    }
}
