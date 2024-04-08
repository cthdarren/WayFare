package com.example.wayfare.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Fragment.CreateListing.CreateListingFragment7;
import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.Models.ConversionRatesModel;
import com.example.wayfare.Models.CurrencyResponseModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Helper {

    public static List<String> languages = Arrays.asList("Abkhaz ", "Afar ", "Afrikaans ", "Akan ", "Albanian ", "Amharic ", "Arabic ", "Aragonese ", "Armenian ", "Assamese ", "Avaric ", "Avestan ", "Aymara ", "Azerbaijani ", "Bambara ", "Bashkir ", "Basque ", "Belarusian ", "Bengali; Bangla ", "Bihari ", "Bislama ", "Bosnian ", "Breton ", "Bulgarian ", "Burmese ", "Catalan; Valencian ", "Chamorro ", "Chechen ", "Chichewa; Chewa; Nyanja ", "Chinese ", "Chuvash ", "Cornish ", "Corsican ", "Cree ", "Croatian ", "Czech ", "Danish ", "Divehi; Dhivehi; Maldivian; ", "Dutch ", "Dzongkha ", "English ", "Esperanto ", "Estonian ", "Ewe ", "Faroese ", "Fijian ", "Finnish ", "French ", "Fula; Fulah; Pulaar; Pular ", "Galician ", "Ganda ", "Georgian ", "German ", "Greek", "Guaraní ", "Gujarati ", "Haitian; Haitian Creole ", "Hausa ", "Hebrew (modern) ", "Herero ", "Hindi ", "Hiri Motu ", "Hungarian ", "Icelandic ", "Ido ", "Igbo ", "Indonesian ", "Interlingua ", "Interlingue ", "Inuktitut ", "Inupiaq ", "Irish ", "Italian ", "Japanese ", "Javanese ", "Kalaallisut", "Kannada ", "Kanuri ", "Kashmiri ", "Kazakh ", "Khmer ", "Kikuyu", "Kinyarwanda ", "Kirundi ", "Komi ", "Kongo ", "Korean ", "Kurdish ", "Kwanyama", "Kyrgyz ", "Lao ", "Latin ", "Latvian ", "Limburgish", "Lingala ", "Lithuanian ", "Luba-Katanga ", "Luxembourgish", "Macedonian ", "Malagasy ", "Malay ", "Malayalam ", "Maltese ", "Manx ", "Marathi (Marāṭhī) ", "Marshallese ", "Mongolian ", "Māori ", "Nauru ", "Navajo", "Ndonga ", "Nepali ", "North Ndebele ", "Northern Sami ", "Norwegian ", "Norwegian Bokmål ", "Norwegian Nynorsk ", "Nuosu ", "Occitan ", "Ojibwe", "Old Church Slavonic", "Oriya ", "Oromo ", "Ossetian", "Panjabi", "Pashto", "Persian (Farsi) ", "Polish ", "Portuguese ", "Pāli ", "Quechua ", "Romanian ", "Romansh ", "Russian ", "Samoan ", "Sango ", "Sanskrit (Saṁskṛta) ", "Sardinian ", "Scottish Gaelic; Gaelic ", "Serbian ", "Shona ", "Sindhi ", "Sinhala", "Slovak ", "Slovene ", "Somali ", "South Azerbaijani ", "South Ndebele ", "Southern Sotho ", "Spanish; Castilian ", "Sundanese ", "Swahili ", "Swati ", "Swedish ", "Tagalog ", "Tahitian ", "Tajik ", "Tamil ", "Tatar ", "Telugu ", "Thai ", "Tibetan Standard", "Tigrinya ", "Tonga (Tonga Islands) ", "Tsonga ", "Tswana ", "Turkish ", "Turkmen ", "Twi ", "Ukrainian ", "Urdu ", "Uyghur", "Uzbek ", "Venda ", "Vietnamese ", "Volapük ", "Walloon ", "Welsh ", "Western Frisian ", "Wolof ", "Xhosa ", "Yiddish ", "Yoruba ", "Zhuang", "Zulu ");
    public static List<String> currencies = Arrays.asList("USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL");
    public static Map conversionRates;
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
    public static void goToFragmentArgs(Bundle args, FragmentManager fm, int fragmentId, Fragment fragment) {
        fragment.setArguments(args);
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
                        R.anim.slide_out_right_to_left,  // exit
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

    public static void goToFullScreenFragmentFromTop(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom, R.anim.slide_in_bottom, R.anim.slide_out_top)
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
        } else if (epochDiff > 60 * 60 * 24 * 30) {
            number = epochDiff / (60 * 60 * 24 * 30);
            timeCategory = "month";
        } else if (epochDiff > 60 * 60 * 24 * 7) {
            number = epochDiff / (60 * 60 * 24 * 7);
            timeCategory = "week";
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

    public static String convert24to12(int hour){
        String timeSuffix = "PM";
        if (hour < 12)
            timeSuffix = "AM";
        if (hour > 12)
            return hour - 12 + timeSuffix;

        return hour + timeSuffix;
    }

    public static Double exchangeToLocal(Double usdPrice, String sharedPrefsCurrencyName){
        BigDecimal exchangeRate = BigDecimal.valueOf((Double)Helper.conversionRates.get(sharedPrefsCurrencyName));
        BigDecimal usd = BigDecimal.valueOf(usdPrice);
        return usd.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }


    public static Double exchangeToUsd(int localPrice, String sharedPrefsCurrencyName){
        BigDecimal exchangeRate = BigDecimal.valueOf((Double)Helper.conversionRates.get(sharedPrefsCurrencyName));
        BigDecimal local = BigDecimal.valueOf(localPrice);
        return local.divide(exchangeRate, 20, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static void getExchangeRate(Context context){
        new AuthService(context).getResponse("/api/v1/getrates", false, RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success) {
                    Gson gson = new Gson();
                    ConversionRatesModel rates = new Gson().fromJson(new Gson().toJson(json.data), ConversionRatesModel.class);
                    conversionRates = gson.fromJson(gson.toJson(rates), Map.class);
                }
                else {
                    Log.d("ERROR EXCHANGE RATE", "FAILED TO GET EXCHANGE RATES");
                }
            }
        });

    }
}
