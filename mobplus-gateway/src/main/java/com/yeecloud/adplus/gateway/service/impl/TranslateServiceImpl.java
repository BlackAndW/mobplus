package com.yeecloud.adplus.gateway.service.impl;

import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.GoogleApiUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Service
public class TranslateServiceImpl implements TranslateService {

    @Override
    public List<String> translationList(TranslateForm form) {

        Translate translate = getTranslateService();

        if (!checkSupportedLang(form.getToLang(), translate)) return null;

        List<Translation> translations =
                translate.translate(
                        form.getSourceList(),
//                        Translate.TranslateOption.sourceLanguage(form.getFromLang()),
                        Translate.TranslateOption.targetLanguage(form.getToLang()),
                        // Use "base" for standard edition, "nmt" for the premium model.
                        Translate.TranslateOption.model("base"));

        List<String> translationList = new ArrayList<>();
        translations.forEach( translation -> {
            translationList.add(translation.getTranslatedText());
        });
        return translationList;
    }

    @Override
    public String translation(TranslateForm form) {
        Translate translate = getTranslateService();
        if (!checkSupportedLang(form.getToLang(), translate)) return null;
        Translation translation =
                translate.translate(
                        form.getSourceString(),
                        Translate.TranslateOption.targetLanguage(form.getToLang()),
                        Translate.TranslateOption.model("base"));
        return translation.getTranslatedText();
    }

    private Translate getTranslateService() {
        // 获取json密钥文件
        File credentialsPath = new File("src/main/resources/translation-test.json");

        Translate translate = TranslateOptions
                .newBuilder()
                .setCredentials(GoogleApiUtil.getCredential(credentialsPath))
                .build()
                .getService();

        return translate;
    }

    private boolean checkSupportedLang(String toLang, Translate translate) {
        List<Language> languages = translate.listSupportedLanguages();
        boolean isSupported = false;
        for (Language language : languages) {
            if (language.getCode().equals(toLang)) {
                isSupported = true;
                break;
            }
        }
        return isSupported;
    }
}