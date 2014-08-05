package util;

import java.util.List;

import methods.ArrayMethods;
import superMiner.Ore;
import superMiner.OreInfo;

public class OreInfoListMethods {

	public static int[] oreInfoListToIdIntArray(List<OreInfo> oreInfoList) {
		int[] OreIds = new int[oreInfoList.size()];
		for (int i = 0; i < OreIds.length; i++) {
			OreIds[i] = oreInfoList.get(i).getId();
		}
		return OreIds;
	}

	/**
	 *
	 * Does not verify that oreInfoList contains anything.
	 * It just ensures that oreInfoList does not contain anything else
	 * @param oreInfoList
	 * @param oreIds
	 * @return
	 */
	public static boolean ListDoesntContainAnythingOtherThan(List<OreInfo> oreInfoList, int... oreIds) {
		for (OreInfo ore : oreInfoList) {
			if (!ArrayMethods.arrayContainsInt(oreIds, ore.getId())) {
				return false;
			}
		}
		return true;
	}

	public static boolean ListContains(List<OreInfo> oreInfoList, Ore ore) {
		for (OreInfo oreInfo : oreInfoList) {
			if (oreInfo.getId() == ore.getId()) {
				return true;
			}
		}
		return false;
	}

}
