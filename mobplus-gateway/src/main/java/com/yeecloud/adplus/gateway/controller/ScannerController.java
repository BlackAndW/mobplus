package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.adplus.gateway.service.ScannerRequestService;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.Base64Util;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Leonard
 * @create: 2021/6/22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/scanner")
public class ScannerController {

    @Resource
    TranslateService translateService;

    @Resource
    ScannerRequestService scannerRequestService;

//    @RequestMapping("/translate/list")
//    public Result translateTextList(@RequestBody ScannerForm form){
//        List<String> result = translateService.translationList(form);
//        if (result == null) { return Result.FAILURE("translation error!"); }
//        return Result.SUCCESS(result);
//    }

    @RequestMapping("/translate")
    public Result translateText(@RequestBody ScannerForm form){
        String result = translateService.translation(form);
        if (result == null) { return Result.FAILURE("translation error!"); }
        return Result.SUCCESS(result);
    }


    @RequestMapping("/test")
    public Result test() throws IOException, ServiceException {
        File file = new File("C:\\Users\\Admin\\Pictures\\3cbd83a0a2195d06363b306e4fe63fd9.jpeg");
        byte[] data = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgStr = Base64Util.encode(data);
        String imgParam = URLEncoder.encode(imgStr, "UTF-8");
        ScannerForm formTest = new ScannerForm();
        formTest.setType(0);
        formTest.setToLang("en");
        formTest.setImageBase64(imgParam);
//        scannerRequestService.resultArray(form.getType(), form.getToLang(), imgParam);
        JSONArray result = scannerRequestService.resultArray(formTest);
        return Result.SUCCESS(result);
    }

    @RequestMapping("testzz")
    public String testzz() {
        String input = "{{short description|Subfamily of aquatic mammals}}\\n{{about|the aquatic mammal}}\\n{{pp-move-indef}}\\n{{Use dmy dates|date=May 2016}}\\n{{Automatic Taxobox\\n| name = Sea lion \\n| fossil_range = Late [[Oligocene]] – [[Holocene]]\\n| image = California sea lion in La Jolla (70568).jpg\\n| image_caption = [[California sea lion]] (''Zalophus californianus'')\\n| taxon = Otariinae\\n| authority = Gray 1825\\n| subdivision_ranks = Genera\\n| subdivision =\\n''[[Eumetopias]]''<br/>\\n''[[Neophoca]]''<br/>\\n''[[Otaria]]''<br/>\\n''[[Phocarctos]]''<br/>\\n''[[Zalophus]]''\\n}}\\n\\n'''Sea lions''' are [[pinniped]]s characterized by external [[pinna (anatomy)|ear flaps]], long foreflippers, the ability to walk on all fours, short, thick hair, and a big chest and belly. Together with the [[fur seal]]s, they comprise the family [[Otariidae]], [[eared seal]]s, which contains six [[extant taxa|extant]] and one extinct species (the [[Japanese sea lion]]) in five [[genera]]. Their range extends from the [[subarctic]] to [[tropical]] waters of the [[Ocean|global ocean]] in both the [[Northern Hemisphere|Northern]] and [[Southern Hemisphere]]s, with the notable exception of the northern [[Atlantic Ocean]].<ref name=seaworld>{{cite web |url=http://seaworld.org/en/Animal-Info/Animal-Bytes/Mammals/California-Sea-Lion |title=California Sea Lion – SeaWorld Info Book |access-date=26 December 2013 |publisher=SeaWorld |archive-url=https://web.archive.org/web/20150414073258/http://seaworld.org/en/animal-info/animal-bytes/mammals/california-sea-lion/ |archive-date=14 April 2015 |url-status=dead |df=dmy-all }}</ref> They have an average lifespan of 20–30 years. A male California sea lion weighs on average about {{convert|300|kg|lb|abbr=on}} and is about {{convert|8|ft|m|abbr=on|order=flip}} long, while the female sea lion weighs {{convert|100|kg|lb|abbr=on}} and is {{convert|6|ft|m|abbr=on|order=flip}} long. The largest sea lion is [[Steller's sea lion]], which can weigh {{convert|1000|kg|lb|abbr=on}} and grow to a length of {{convert|10|ft|m|abbr=on|order=flip}}. Sea lions consume large quantities of food at a time and are known to eat about 5–8% of their body weight (about {{convert|15|-|35|lb|kg|abbr=on|order=flip}}) at a single feeding. Sea lions can move around {{convert|16|knots|km/h mph}} in water and at their fastest they can reach a speed of about {{convert|30|knots|km/h mph}}.<ref>{{cite book|last1=Riedman|first1=Marianne|title=The Pinnipeds: Seals, Sea lions, and Walruses|url=https://archive.org/details/pinnipedssealsse0000ried|url-access=registration|date=13 December 1989|publisher=University of California Press|isbn=9780520064973|page=[https://archive.org/details/pinnipedssealsse0000ried/page/7 7]}}</ref> Three species, the [[Australian sea lion]], the [[Galápagos sea lion]] and the [[New Zealand sea lion]], are listed as [[IUCN Red List endangered species (Animalia)|endangered]].<ref>{{Cite journal|last=Chilvers|first=B. L.|author-link=Louise Chilvers|date=2015|title=Phocarctos hookeri. The IUCN Red List of Threatened Species|url=|journal=International Union for Conservation of Nature and Natural Resources|volume=|pages=|doi=10.2305/IUCN.UK.2015-2.RLTS.T17026A1306343.en|via=}}</ref><ref>{{Cite journal|last=Trillmich|first=F.|date=2015|title=Arctocephalus galapagoensis. The IUCN Red List of Threatened Species|journal=International Union for Conservation of Nature and Natural Resources|doi=10.2305/IUCN.UK.2015-2.RLTS.T2057A45223722.en}}</ref><ref>{{Cite journal|last=Goldsworthy|first=S. D.|date=2015|title=Neophoca cinerea. The IUCN Red List of Threatened Species|journal=International Union for Conservation of Nature and Natural Resources|doi=10.2305/IUCN.UK.2015-2.RLTS.T14549A45228341.en}}</ref>";
        String regex = "'''|(<ref.*?</ref>)|\\[|]|.*?(?='''.*''')|\\{\\{.*?}}|「|」";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return StringUtils.trim(matcher.replaceAll(""));
    }

    @RequestMapping("/imageScan")
    public Result test(@RequestBody ScannerForm form) throws IOException, ServiceException {
        String imgParam = URLEncoder.encode(form.getImageBase64(), "UTF-8");
        form.setImageBase64(imgParam);
//        scannerRequestService.resultArray(form.getType(), form.getToLang(), imgParam);
        JSONArray result = scannerRequestService.resultArray(form);
        return Result.SUCCESS(result);
    }
}
