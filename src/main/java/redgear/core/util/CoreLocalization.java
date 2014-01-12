package redgear.core.util;

import java.net.URL;

import redgear.core.mod.IPlugin;
import redgear.core.mod.ModUtils;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CoreLocalization implements IPlugin {

	private final String[] langs = {"en_US", "af_ZA", "ar_SA", "bg_BG", "ca_ES", "cs_CZ", "cy_GB", "da_DK", "de_DE",
			"el_GR", "en_AU", "en_CA", "en_GB", "en_PT", "eo_UY", "es_AR", "es_ES", "es_MX", "es_UY", "es_VE", "et_EE",
			"fi_FI", "fr_CA", "fr_FR", "ga_IE", "gl_ES", "he_IL", "hi_IN", "hr_HR", "hu_HU", "hy_AM", "id_ID", "is_IS",
			"it_IT", "ja_JP", "ka_GE", "ko_KR", "lt_LT", "lv_LV", "ms_MY", "mt_MT", "nb_NO", "nl_NL", "nn_NO", "no_NO",
			"pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "sk_SK", "sl_SI", "sr_SP", "sv_SE", "th_TH", "tr_TR", "uk_UA",
			"vi_VN", "zh_CN", "zh_TW", };

	@Override
	public void preInit(ModUtils inst) {
		final String id = inst.modId;
		String fileName;

		for (String lang : langs) {
			fileName = StringHelper.parseLangFile(id, lang);
			URL urlResource = this.getClass().getResource(fileName);
			if (urlResource != null)
				LanguageRegistry.instance().loadLocalization(fileName, lang, true);
		}
	}

	@Override
	public void Init(ModUtils inst) {

	}

	@Override
	public void postInit(ModUtils inst) {

	}

}
