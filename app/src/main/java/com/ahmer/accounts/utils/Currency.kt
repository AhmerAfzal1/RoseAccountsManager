package com.ahmer.accounts.utils

sealed class Currency(
    val code: String, val symbol: String, val flag: String
) {
    data object AED : Currency(code = "AED", symbol = "د.إ", flag = "\uD83C\uDDE6\uD83C\uDDEA")
    data object AFN : Currency(code = "AFN", symbol = "؋", flag = "\uD83C\uDDE6\uD83C\uDDEB")
    data object ALL : Currency(code = "ALL", symbol = "L", flag = "\uD83C\uDDE6\uD83C\uDDF1")
    data object AMD : Currency(code = "AMD", symbol = "֏", flag = "\uD83C\uDDE6\uD83C\uDDF2")
    data object AOA : Currency(code = "AOA", symbol = "Kz", flag = "\uD83C\uDDE6\uD83C\uDDF4")
    data object ARS : Currency(code = "ARS", symbol = "$", flag = "\uD83C\uDDE6\uD83C\uDDF7")
    data object AUD : Currency(code = "AUD", symbol = "$", flag = "\uD83C\uDDE6\uD83C\uDDFA")
    data object AZN : Currency(code = "AZN", symbol = "m", flag = "\uD83C\uDDE6\uD83C\uDDFF")
    data object BAM : Currency(code = "BAM", symbol = "KM", flag = "\uD83C\uDDE7\uD83C\uDDE6")
    data object BBD : Currency(code = "BBD", symbol = "$", flag = "\uD83C\uDDE7\uD83C\uDDE7")
    data object BDT : Currency(code = "BDT", symbol = "Tk", flag = "\uD83C\uDDE7\uD83C\uDDE9")
    data object BGN : Currency(code = "BGN", symbol = "лв", flag = "\uD83C\uDDE7\uD83C\uDDEC")
    data object BHD : Currency(code = "BHD", symbol = "BD", flag = "\uD83C\uDDE7\uD83C\uDDED")
    data object BIF : Currency(code = "BIF", symbol = "FBu", flag = "\uD83C\uDDE7\uD83C\uDDEE")
    data object BMD : Currency(code = "BMD", symbol = "$", flag = "\uD83C\uDDE7\uD83C\uDDF2")
    data object BND : Currency(code = "BND", symbol = "$", flag = "\uD83C\uDDE7\uD83C\uDDF3")
    data object BOB : Currency(code = "BOB", symbol = "Bs", flag = "\uD83C\uDDE7\uD83C\uDDF4")
    data object BRL : Currency(code = "BRL", symbol = "R$", flag = "\uD83C\uDDE7\uD83C\uDDF7")
    data object BSD : Currency(code = "BSD", symbol = "B$", flag = "\uD83C\uDDE7\uD83C\uDDF8")
    data object BTN : Currency(code = "BTN", symbol = "Nu.", flag = "\uD83C\uDDE7\uD83C\uDDF9")
    data object BWP : Currency(code = "BWP", symbol = "P", flag = "\uD83C\uDDE7\uD83C\uDDFC")
    data object BYN : Currency(code = "BYN", symbol = "Br", flag = "\uD83C\uDDE7\uD83C\uDDFE")
    data object BZD : Currency(code = "BZD", symbol = "BZ$", flag = "\uD83C\uDDE7\uD83C\uDDFF")
    data object CAD : Currency(code = "CAD", symbol = "$", flag = "\uD83C\uDDE8\uD83C\uDDE6")
    data object CDF : Currency(code = "CDF", symbol = "FC", flag = "\uD83C\uDDE8\uD83C\uDDE9")
    data object CHF : Currency(code = "CHF", symbol = "CHF", flag = "\uD83C\uDDE8\uD83C\uDDED")
    data object CLP : Currency(code = "CLP", symbol = "$", flag = "\uD83C\uDDE8\uD83C\uDDF1")
    data object CNY : Currency(code = "CNY", symbol = "¥", flag = "\uD83C\uDDE8\uD83C\uDDF3")
    data object COP : Currency(code = "COP", symbol = "$", flag = "\uD83C\uDDE8\uD83C\uDDF4")
    data object CRC : Currency(code = "CRC", symbol = "₡", flag = "\uD83C\uDDE8\uD83C\uDDF7")
    data object CUP : Currency(code = "CUP", symbol = "$", flag = "\uD83C\uDDE8\uD83C\uDDFA")
    data object CVE : Currency(code = "CVE", symbol = "$", flag = "\uD83C\uDDE8\uD83C\uDDFB")
    data object CZK : Currency(code = "CZK", symbol = "Kč", flag = "\uD83C\uDDE8\uD83C\uDDFF")
    data object DJF : Currency(code = "DJF", symbol = "Fdj", flag = "\uD83C\uDDE9\uD83C\uDDEF")
    data object DKK : Currency(code = "DKK", symbol = "kr", flag = "\uD83C\uDDE9\uD83C\uDDF0")
    data object DOP : Currency(code = "DOP", symbol = "RD$", flag = "\uD83C\uDDE9\uD83C\uDDF4")
    data object DZD : Currency(code = "DZD", symbol = "دج", flag = "\uD83C\uDDE9\uD83C\uDDFF")
    data object EGP : Currency(code = "EGP", symbol = "£", flag = "\uD83C\uDDEA\uD83C\uDDEC")
    data object ERN : Currency(code = "ERN", symbol = "نافكا", flag = "\uD83C\uDDEA\uD83C\uDDF7")
    data object ETB : Currency(code = "ETB", symbol = "Br", flag = "\uD83C\uDDEA\uD83C\uDDF9")
    data object EUR : Currency(code = "EUR", symbol = "€", flag = "\uD83C\uDDEA\uD83C\uDDFA")
    data object FJB : Currency(code = "FJB", symbol = "FJ$", flag = "\uD83C\uDDEB\uD83C\uDDEF")
    data object GBP : Currency(code = "GBP", symbol = "£", flag = "\uD83C\uDDEC\uD83C\uDDE7")
    data object GEL : Currency(code = "GEL", symbol = "₾", flag = "\uD83C\uDDEC\uD83C\uDDEA")
    data object GHS : Currency(code = "GHS", symbol = "¢", flag = "\uD83C\uDDEC\uD83C\uDDED")
    data object GMD : Currency(code = "GMD", symbol = "D", flag = "\uD83C\uDDEC\uD83C\uDDF2")
    data object GNF : Currency(code = "GNF", symbol = "FG", flag = "\uD83C\uDDEC\uD83C\uDDF3")
    data object GTQ : Currency(code = "GTQ", symbol = "Q", flag = "\uD83C\uDDEC\uD83C\uDDF9")
    data object GYD : Currency(code = "GYD", symbol = "$", flag = "\uD83C\uDDEC\uD83C\uDDFE")
    data object HKD : Currency(code = "HKD", symbol = "$", flag = "\uD83C\uDDED\uD83C\uDDF0")
    data object HNL : Currency(code = "HNL", symbol = "L", flag = "\uD83C\uDDED\uD83C\uDDF3")
    data object HRK : Currency(code = "HRK", symbol = "kn", flag = "\uD83C\uDDED\uD83C\uDDF7")
    data object HTG : Currency(code = "HTG", symbol = "G", flag = "\uD83C\uDDED\uD83C\uDDF9")
    data object HUF : Currency(code = "HUF", symbol = "Ft", flag = "\uD83C\uDDED\uD83C\uDDFA")
    data object IDR : Currency(code = "IDR", symbol = "Rp", flag = "\uD83C\uDDEE\uD83C\uDDE9")
    data object ILS : Currency(code = "ILS", symbol = "₪", flag = "\uD83C\uDDEE\uD83C\uDDF1")
    data object INR : Currency(code = "INR", symbol = "₹", flag = "\uD83C\uDDEE\uD83C\uDDF3")
    data object IQD : Currency(code = "IQD", symbol = "ع.د", flag = "\uD83C\uDDEE\uD83C\uDDF6")
    data object IRR : Currency(code = "IRR", symbol = "﷼", flag = "\uD83C\uDDEE\uD83C\uDDF7")
    data object ISK : Currency(code = "ISK", symbol = "kr", flag = "\uD83C\uDDEE\uD83C\uDDF8")
    data object JMD : Currency(code = "JMD", symbol = "J$", flag = "\uD83C\uDDEF\uD83C\uDDF2")
    data object JOD : Currency(code = "JOD", symbol = "د.ا", flag = "\uD83C\uDDEF\uD83C\uDDF4")
    data object JPY : Currency(code = "JPY", symbol = "¥", flag = "\uD83C\uDDEF\uD83C\uDDF5")
    data object KES : Currency(code = "KES", symbol = "KSh", flag = "\uD83C\uDDF0\uD83C\uDDEA")
    data object KGS : Currency(code = "KGS", symbol = "Лв", flag = "\uD83C\uDDF0\uD83C\uDDEC")
    data object KHR : Currency(code = "KHR", symbol = "៛", flag = "\uD83C\uDDF0\uD83C\uDDED")
    data object KMF : Currency(code = "KMF", symbol = "CF", flag = "\uD83C\uDDF0\uD83C\uDDF2")
    data object KPW : Currency(code = "KPW", symbol = "₩", flag = "\uD83C\uDDF0\uD83C\uDDF5")
    data object KRW : Currency(code = "KRW", symbol = "₩", flag = "\uD83C\uDDF0\uD83C\uDDF7")
    data object KWD : Currency(code = "KWD", symbol = "د.ك", flag = "\uD83C\uDDF0\uD83C\uDDFC")
    data object KYD : Currency(code = "KYD", symbol = "$", flag = "\uD83C\uDDF0\uD83C\uDDFE")
    data object KZT : Currency(code = "KZT", symbol = "лв", flag = "\uD83C\uDDF0\uD83C\uDDFF")
    data object LAK : Currency(code = "LAK", symbol = "₭", flag = "\uD83C\uDDF1\uD83C\uDDE6")
    data object LBP : Currency(code = "LBP", symbol = "ل.ل.", flag = "\uD83C\uDDF1\uD83C\uDDE7")
    data object LKR : Currency(code = "LKR", symbol = "₨", flag = "\uD83C\uDDF1\uD83C\uDDF0")
    data object LRD : Currency(code = "LRD", symbol = "$", flag = "\uD83C\uDDF1\uD83C\uDDF7")
    data object LSL : Currency(code = "LSL", symbol = "M", flag = "\uD83C\uDDF1\uD83C\uDDF8")
    data object LTL : Currency(code = "LTL", symbol = "Lt", flag = "\uD83C\uDDF1\uD83C\uDDF9")
    data object LYD : Currency(code = "LYD", symbol = "ل.د", flag = "\uD83C\uDDF1\uD83C\uDDFE")
    data object MAD : Currency(code = "MAD", symbol = "MAD", flag = "\uD83C\uDDF2\uD83C\uDDE6")
    data object MDL : Currency(code = "MDL", symbol = "MDL", flag = "\uD83C\uDDF2\uD83C\uDDE9")
    data object MGA : Currency(code = "MGA", symbol = "Ar", flag = "\uD83C\uDDF2\uD83C\uDDEC")
    data object MKD : Currency(code = "MKD", symbol = "ден", flag = "\uD83C\uDDF2\uD83C\uDDF0")
    data object MMK : Currency(code = "MMK", symbol = "K", flag = "\uD83C\uDDF2\uD83C\uDDF2")
    data object MNT : Currency(code = "MNT", symbol = "₮", flag = "\uD83C\uDDF2\uD83C\uDDF3")
    data object MRO : Currency(code = "MRO", symbol = "UM", flag = "\uD83C\uDDF2\uD83C\uDDF7")
    data object MUR : Currency(code = "MUR", symbol = "₨", flag = "\uD83C\uDDF2\uD83C\uDDF7")
    data object MVR : Currency(code = "MVR", symbol = "Rf", flag = "\uD83C\uDDF2\uD83C\uDDFB")
    data object MWK : Currency(code = "MWK", symbol = "MK", flag = "\uD83C\uDDF2\uD83C\uDDFC")
    data object MXN : Currency(code = "MXN", symbol = "$", flag = "\uD83C\uDDF2\uD83C\uDDFD")
    data object MYR : Currency(code = "MYR", symbol = "RM", flag = "\uD83C\uDDF2\uD83C\uDDFE")
    data object MZN : Currency(code = "MZN", symbol = "MT", flag = "\uD83C\uDDF2\uD83C\uDDFF")
    data object NAD : Currency(code = "NAD", symbol = "$", flag = "\uD83C\uDDF3\uD83C\uDDE6")
    data object NGN : Currency(code = "NGN", symbol = "₦", flag = "\uD83C\uDDF3\uD83C\uDDEC")
    data object NIO : Currency(code = "NIO", symbol = "C$", flag = "\uD83C\uDDF3\uD83C\uDDEE")
    data object NOK : Currency(code = "NOK", symbol = "kr", flag = "\uD83C\uDDF3\uD83C\uDDF4")
    data object NPR : Currency(code = "NPR", symbol = "₨", flag = "\uD83C\uDDF3\uD83C\uDDF5")
    data object NZD : Currency(code = "NZD", symbol = "$", flag = "\uD83C\uDDF3\uD83C\uDDFF")
    data object OMR : Currency(code = "OMR", symbol = "﷼", flag = "\uD83C\uDDF4\uD83C\uDDF2")
    data object PAB : Currency(code = "PAB", symbol = "B/.", flag = "\uD83C\uDDF5\uD83C\uDDE6")
    data object PEN : Currency(code = "PEN", symbol = "S/.", flag = "\uD83C\uDDF5\uD83C\uDDEA")
    data object PGK : Currency(code = "PGK", symbol = "K", flag = "\uD83C\uDDF5\uD83C\uDDEC")
    data object PHP : Currency(code = "PHP", symbol = "₱", flag = "\uD83C\uDDF5\uD83C\uDDED")
    data object PKR : Currency(code = "PKR", symbol = "₨", flag = "\uD83C\uDDF5\uD83C\uDDF0")
    data object PLN : Currency(code = "PLN", symbol = "zł", flag = "\uD83C\uDDF5\uD83C\uDDF1")
    data object PYG : Currency(code = "PYG", symbol = "Gs", flag = "\uD83C\uDDF5\uD83C\uDDFE")
    data object QAR : Currency(code = "QAR", symbol = "﷼", flag = "\uD83C\uDDF6\uD83C\uDDE6")
    data object RON : Currency(code = "RON", symbol = "lei", flag = "\uD83C\uDDF7\uD83C\uDDF4")
    data object RSD : Currency(code = "RSD", symbol = "Дин.", flag = "\uD83C\uDDF7\uD83C\uDDF8")
    data object RUB : Currency(code = "RUB", symbol = "₽", flag = "\uD83C\uDDF7\uD83C\uDDFA")
    data object RWF : Currency(code = "RWF", symbol = "FRw", flag = "\uD83C\uDDF7\uD83C\uDDFC")
    data object SAR : Currency(code = "SAR", symbol = "﷼", flag = "\uD83C\uDDF8\uD83C\uDDE6")
    data object SBD : Currency(code = "SBD", symbol = "Si$", flag = "\uD83C\uDDF8\uD83C\uDDE7")
    data object SCR : Currency(code = "SCR", symbol = "SR", flag = "\uD83C\uDDF8\uD83C\uDDE8")
    data object SDG : Currency(code = "SDG", symbol = "ج.س.", flag = "\uD83C\uDDF8\uD83C\uDDE9")
    data object SEK : Currency(code = "SEK", symbol = "kr", flag = "\uD83C\uDDF8\uD83C\uDDEA")
    data object SGD : Currency(code = "SGD", symbol = "$", flag = "\uD83C\uDDF8\uD83C\uDDEC")
    data object SLL : Currency(code = "SLL", symbol = "Le", flag = "\uD83C\uDDF8\uD83C\uDDF1")
    data object SOS : Currency(code = "SOS", symbol = "S", flag = "\uD83C\uDDF8\uD83C\uDDF4")
    data object SRD : Currency(code = "SRD", symbol = "$", flag = "\uD83C\uDDF8\uD83C\uDDF7")
    data object SSP : Currency(code = "SSP", symbol = "£", flag = "\uD83C\uDDF8\uD83C\uDDF8")
    data object STD : Currency(code = "STD", symbol = "Db", flag = "\uD83C\uDDF8\uD83C\uDDF9")
    data object SYP : Currency(code = "SYP", symbol = "LS", flag = "\uD83C\uDDF8\uD83C\uDDFE")
    data object SZL : Currency(code = "SZL", symbol = "E", flag = "\uD83C\uDDF8\uD83C\uDDFF")
    data object THB : Currency(code = "THB", symbol = "฿", flag = "\uD83C\uDDF9\uD83C\uDDED")
    data object TJS : Currency(code = "TJS", symbol = "ЅM", flag = "\uD83C\uDDF9\uD83C\uDDEF")
    data object TMT : Currency(code = "TMT", symbol = "T", flag = "\uD83C\uDDF9\uD83C\uDDF2")
    data object TND : Currency(code = "TND", symbol = "د.ت", flag = "\uD83C\uDDF9\uD83C\uDDF3")
    data object TOP : Currency(code = "TOP", symbol = "PT", flag = "\uD83C\uDDF9\uD83C\uDDF4")
    data object TRY : Currency(code = "TRY", symbol = "₺", flag = "\uD83C\uDDF9\uD83C\uDDF7")
    data object TTD : Currency(code = "TTD", symbol = "TT$", flag = "\uD83C\uDDF9\uD83C\uDDF9")
    data object TWD : Currency(code = "TWD", symbol = "NT$", flag = "\uD83C\uDDF9\uD83C\uDDFC")
    data object TZS : Currency(code = "TZS", symbol = "TSh", flag = "\uD83C\uDDF9\uD83C\uDDFF")
    data object UAH : Currency(code = "UAH", symbol = "₴", flag = "\uD83C\uDDFA\uD83C\uDDE6")
    data object UGX : Currency(code = "UGX", symbol = "USh", flag = "\uD83C\uDDFA\uD83C\uDDEC")
    data object USD : Currency(code = "USD", symbol = "$", flag = "\uD83C\uDDFA\uD83C\uDDF8")
    data object UYU : Currency(code = "UYU", symbol = "$", flag = "\uD83C\uDDFA\uD83C\uDDFE")
    data object UZS : Currency(code = "UZS", symbol = "so'm", flag = "\uD83C\uDDFA\uD83C\uDDFF")
    data object VEF : Currency(code = "VEF", symbol = "Bs", flag = "\uD83C\uDDFB\uD83C\uDDEA")
    data object VND : Currency(code = "VND", symbol = "₫", flag = "\uD83C\uDDFB\uD83C\uDDF3")
    data object VUV : Currency(code = "VUV", symbol = "VT", flag = "\uD83C\uDDFB\uD83C\uDDFA")
    data object WST : Currency(code = "WST", symbol = "SAT", flag = "\uD83C\uDDFC\uD83C\uDDF8")
    data object XAF : Currency(code = "XAF", symbol = "FCFA", flag = "\uD83C\uDF0D")
    data object XCD : Currency(code = "XCD", symbol = "$", flag = "\uD83C\uDF0E")
    data object XOF : Currency(code = "XOF", symbol = "CFA", flag = "\uD83C\uDF0D")
    data object YER : Currency(code = "YER", symbol = "﷼", flag = "\uD83C\uDDFE\uD83C\uDDEA")
    data object ZAR : Currency(code = "ZAR", symbol = "R", flag = "🇿🇦")
    data object ZMW : Currency(code = "ZMW", symbol = "ZK", flag = "\uD83C\uDDFF\uD83C\uDDF2")

    companion object {
        val listOfCurrencies: List<Currency> by lazy {
            listOf(
                AED, AFN, ALL, AMD, AOA, ARS, AUD, AZN, BAM, BBD, BDT, BGN, BHD, BIF, BMD, BND, BOB,
                BRL, BSD, BTN, BWP, BYN, BZD, CAD, CDF, CHF, CLP, CNY, COP, CRC, CUP, CVE, CZK, DJF,
                DKK, DOP, DZD, EGP, ERN, ETB, EUR, FJB, GBP, GEL, GHS, GMD, GNF, GTQ, GYD, HKD, HNL,
                HRK, HTG, HUF, IDR, ILS, INR, IQD, IRR, ISK, JMD, JOD, JPY, KES, KGS, KHR, KMF, KPW,
                KRW, KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LTL, LYD, MAD, MDL, MGA, MKD, MMK, MNT,
                MRO, MUR, MVR, MWK, MXN, MYR, MZN, NAD, NGN, NIO, NOK, NPR, NZD, OMR, PAB, PEN, PGK,
                PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RWF, SAR, SBD, SCR, SDG, SEK, SGD, SLL, SOS,
                SRD, SSP, STD, SYP, SZL, THB, TJS, TMT, TND, TOP, TRY, TTD, TWD, TZS, UAH, UGX, USD,
                UYU, UZS, VEF, VND, VUV, WST, XAF, XCD, XOF, YER, ZAR, ZMW
            )
        }

        fun valueOf(code: String): Currency {
            return when (code) {
                AED.code -> AED
                AFN.code -> AFN
                ALL.code -> ALL
                AMD.code -> AMD
                AOA.code -> AOA
                ARS.code -> ARS
                AUD.code -> AUD
                AZN.code -> AZN
                BAM.code -> BAM
                BBD.code -> BBD
                BDT.code -> BDT
                BGN.code -> BGN
                BHD.code -> BHD
                BIF.code -> BIF
                BMD.code -> BMD
                BND.code -> BND
                BOB.code -> BOB
                BRL.code -> BRL
                BSD.code -> BSD
                BTN.code -> BTN
                BWP.code -> BWP
                BYN.code -> BYN
                BZD.code -> BZD
                CAD.code -> CAD
                CDF.code -> CDF
                CHF.code -> CHF
                CLP.code -> CLP
                CNY.code -> CNY
                COP.code -> COP
                CRC.code -> CRC
                CUP.code -> CUP
                CVE.code -> CVE
                CZK.code -> CZK
                DJF.code -> DJF
                DKK.code -> DKK
                DOP.code -> DOP
                DZD.code -> DZD
                EGP.code -> EGP
                ERN.code -> ERN
                ETB.code -> ETB
                EUR.code -> EUR
                FJB.code -> FJB
                GBP.code -> GBP
                GEL.code -> GEL
                GHS.code -> GHS
                GMD.code -> GMD
                GNF.code -> GNF
                GTQ.code -> GTQ
                GYD.code -> GYD
                HKD.code -> HKD
                HNL.code -> HNL
                HRK.code -> HRK
                HTG.code -> HTG
                HUF.code -> HUF
                IDR.code -> IDR
                ILS.code -> ILS
                INR.code -> INR
                IQD.code -> IQD
                IRR.code -> IRR
                ISK.code -> ISK
                JMD.code -> JMD
                JOD.code -> JOD
                JPY.code -> JPY
                KES.code -> KES
                KGS.code -> KGS
                KHR.code -> KHR
                KMF.code -> KMF
                KPW.code -> KPW
                KRW.code -> KRW
                KWD.code -> KWD
                KYD.code -> KYD
                KZT.code -> KZT
                LAK.code -> LAK
                LBP.code -> LBP
                LKR.code -> LKR
                LRD.code -> LRD
                LSL.code -> LSL
                LTL.code -> LTL
                LYD.code -> LYD
                MAD.code -> MAD
                MDL.code -> MDL
                MGA.code -> MGA
                MKD.code -> MKD
                MMK.code -> MMK
                MNT.code -> MNT
                MRO.code -> MRO
                MUR.code -> MUR
                MVR.code -> MVR
                MWK.code -> MWK
                MXN.code -> MXN
                MYR.code -> MYR
                MZN.code -> MZN
                NAD.code -> NAD
                NGN.code -> NGN
                NIO.code -> NIO
                NOK.code -> NOK
                NPR.code -> NPR
                NZD.code -> NZD
                OMR.code -> OMR
                PAB.code -> PAB
                PEN.code -> PEN
                PGK.code -> PGK
                PHP.code -> PHP
                PKR.code -> PKR
                PLN.code -> PLN
                PYG.code -> PYG
                QAR.code -> QAR
                RON.code -> RON
                RSD.code -> RSD
                RUB.code -> RUB
                RWF.code -> RWF
                SAR.code -> SAR
                SBD.code -> SBD
                SCR.code -> SCR
                SDG.code -> SDG
                SEK.code -> SEK
                SGD.code -> SGD
                SLL.code -> SLL
                SOS.code -> SOS
                SRD.code -> SRD
                SSP.code -> SSP
                STD.code -> STD
                SYP.code -> SYP
                SZL.code -> SZL
                THB.code -> THB
                TJS.code -> TJS
                TMT.code -> TMT
                TND.code -> TND
                TOP.code -> TOP
                TRY.code -> TRY
                TTD.code -> TTD
                TWD.code -> TWD
                TZS.code -> TZS
                UAH.code -> UAH
                UGX.code -> UGX
                USD.code -> USD
                UYU.code -> UYU
                UZS.code -> UZS
                VEF.code -> VEF
                VND.code -> VND
                VUV.code -> VUV
                WST.code -> WST
                XAF.code -> XAF
                XCD.code -> XCD
                XOF.code -> XOF
                YER.code -> YER
                ZAR.code -> ZAR
                ZMW.code -> ZMW
                else -> PKR
            }
        }
    }
}