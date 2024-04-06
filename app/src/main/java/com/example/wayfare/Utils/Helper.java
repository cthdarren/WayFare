package com.example.wayfare.Utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {
    public static void goToLogin(FragmentManager fm) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .add(R.id.container, new SignInFragment())
                .addToBackStack(SignInFragment.class.getName())
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFragment(FragmentManager fm, int fragmentId, Fragment fragment) {
        fm.beginTransaction()
                .replace(fragmentId, fragment)
                .addToBackStack(fragment.getClass().getName())
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFragmentSlideInRight(FragmentManager fm, int fragmentId, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_left_to_right, // enter
                        R.anim.slide_right_to_left, // exit
                        R.anim.fade_in, // popEnter
                        R.anim.slide_out_left_to_right// popExit
                )
                .add(fragmentId, fragment)
                .addToBackStack(fragment.getClass().getName())
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFragmentSlideInRightArgs(Bundle args, FragmentManager fm, int fragmentId, Fragment fragment) {
        fragment.setArguments(args);
        fm.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_left_to_right, // enter
                        R.anim.slide_right_to_left, // exit
                        R.anim.fade_in, // popEnter
                        R.anim.slide_out_left_to_right// popExit
                )
                .add(fragmentId, fragment)
                .addToBackStack(fragment.getClass().getName())
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFullScreenFragmentFromBottom(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .setReorderingAllowed(true)
                .commit();
    }

    public enum RequestType {
        REQ_GET,
        REQ_POST
    }

    public static String getDifferenceInTimeString(Instant firstInstant, Instant secondInstant) {
        long number;
        String timeCategory;

        long epochDiff = firstInstant.getEpochSecond() - secondInstant.getEpochSecond();
        if (epochDiff > 60 * 60 * 24 * 365) {
            number = epochDiff / (60 * 60 * 24 * 365);
            timeCategory = "year";
        } else if (epochDiff > 60 * 60 * 24 * 12) {
            number = epochDiff / (60 * 60 * 24 * 12);
            timeCategory = "month";
        } else if (epochDiff > 60 * 60 * 24) {
            number = epochDiff / (60 * 60 * 24);
            timeCategory = "day";
        } else if (epochDiff > 60 * 60) {
            number = epochDiff / (60 * 60);
            timeCategory = "hour";
        } else if (epochDiff > 60) {
            number = epochDiff / 60;
            timeCategory = "minute";
        } else {
            number = epochDiff;
            timeCategory = "second";
        }
        if (number > 1) {
            timeCategory += "s";
        }
        return String.format("%s %s", number, timeCategory);
    }

    public static List<String> languages = Arrays.asList("Abkhaz ", "Afar ", "Afrikaans ", "Akan ", "Albanian ", "Amharic ", "Arabic ", "Aragonese ", "Armenian ", "Assamese ", "Avaric ", "Avestan ", "Aymara ", "Azerbaijani ", "Bambara ", "Bashkir ", "Basque ", "Belarusian ", "Bengali; Bangla ", "Bihari ", "Bislama ", "Bosnian ", "Breton ", "Bulgarian ", "Burmese ", "Catalan; Valencian ", "Chamorro ", "Chechen ", "Chichewa; Chewa; Nyanja ", "Chinese ", "Chuvash ", "Cornish ", "Corsican ", "Cree ", "Croatian ", "Czech ", "Danish ", "Divehi; Dhivehi; Maldivian; ", "Dutch ", "Dzongkha ", "English ", "Esperanto ", "Estonian ", "Ewe ", "Faroese ", "Fijian ", "Finnish ", "French ", "Fula; Fulah; Pulaar; Pular ", "Galician ", "Ganda ", "Georgian ", "German ", "Greek", "Guaraní ", "Gujarati ", "Haitian; Haitian Creole ", "Hausa ", "Hebrew (modern) ", "Herero ", "Hindi ", "Hiri Motu ", "Hungarian ", "Icelandic ", "Ido ", "Igbo ", "Indonesian ", "Interlingua ", "Interlingue ", "Inuktitut ", "Inupiaq ", "Irish ", "Italian ", "Japanese ", "Javanese ", "Kalaallisut", "Kannada ", "Kanuri ", "Kashmiri ", "Kazakh ", "Khmer ", "Kikuyu", "Kinyarwanda ", "Kirundi ", "Komi ", "Kongo ", "Korean ", "Kurdish ", "Kwanyama", "Kyrgyz ", "Lao ", "Latin ", "Latvian ", "Limburgish", "Lingala ", "Lithuanian ", "Luba-Katanga ", "Luxembourgish", "Macedonian ", "Malagasy ", "Malay ", "Malayalam ", "Maltese ", "Manx ", "Marathi (Marāṭhī) ", "Marshallese ", "Mongolian ", "Māori ", "Nauru ", "Navajo", "Ndonga ", "Nepali ", "North Ndebele ", "Northern Sami ", "Norwegian ", "Norwegian Bokmål ", "Norwegian Nynorsk ", "Nuosu ", "Occitan ", "Ojibwe", "Old Church Slavonic", "Oriya ", "Oromo ", "Ossetian", "Panjabi", "Pashto", "Persian (Farsi) ", "Polish ", "Portuguese ", "Pāli ", "Quechua ", "Romanian ", "Romansh ", "Russian ", "Samoan ", "Sango ", "Sanskrit (Saṁskṛta) ", "Sardinian ", "Scottish Gaelic; Gaelic ", "Serbian ", "Shona ", "Sindhi ", "Sinhala", "Slovak ", "Slovene ", "Somali ", "South Azerbaijani ", "South Ndebele ", "Southern Sotho ", "Spanish; Castilian ", "Sundanese ", "Swahili ", "Swati ", "Swedish ", "Tagalog ", "Tahitian ", "Tajik ", "Tamil ", "Tatar ", "Telugu ", "Thai ", "Tibetan Standard", "Tigrinya ", "Tonga (Tonga Islands) ", "Tsonga ", "Tswana ", "Turkish ", "Turkmen ", "Twi ", "Ukrainian ", "Urdu ", "Uyghur", "Uzbek ", "Venda ", "Vietnamese ", "Volapük ", "Walloon ", "Welsh ", "Western Frisian ", "Wolof ", "Xhosa ", "Yiddish ", "Yoruba ", "Zhuang", "Zulu ");
}
