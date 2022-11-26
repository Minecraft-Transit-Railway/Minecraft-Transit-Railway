package mtr.screen;

import mtr.client.ClientData;
import mtr.data.NameColorDataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CompanyDashboardList extends DashboardList{
	public <T> CompanyDashboardList() {
		super(null, null, null, null, CompanyDashboardList::onAdd, CompanyDashboardList::onDelete, null, () -> ClientData.DASHBOARD_SEARCH, text -> ClientData.DASHBOARD_SEARCH = text);
	}

	private static void onAdd(NameColorDataBase nameColorDataBase, Integer integer) {
	}

	public static void onDelete(NameColorDataBase nameColorDataBase, Integer integer) {
	}

	public void setData(Set<? extends NameColorDataBase> dataSet, CompanyDashboardScreen.SelectedSubTab tab) {
		List<? extends NameColorDataBase> dataList = new ArrayList<>(dataSet);
		Collections.sort(dataList);
		boolean hasAdd = false;
		boolean hasDelete = false;
		switch (tab) {
			case STATIONS_CLAIMED:
			case DEPOTS_CLAIMED:
			case ROUTES_CLAIMED:
				hasDelete = true;
				break;
			case STATIONS_UNCLAIMED:
			case DEPOTS_UNCLAIMED:
			case ROUTES_UNCLAIMED:
				hasAdd = true;
				break;
		}
		setData(dataList, false, false, false, false, hasAdd, hasDelete);
	}
}
