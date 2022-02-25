package general;

import classBase.List;

public class Area {

	enum BuildingSlotStatus {
		EMPTY, FACTORY
	}
	
	public final String NAME;
	public final int LABEL_X, LABEL_Y, ADDITIONAL_LABEL_WIDTH;
	private List<BuildingSlot> buildingSlots;

	public Area(String name, int labelX, int labelY, int additionalLabelWidth) {
		NAME = name;
		LABEL_X = labelX;
		LABEL_Y = labelY;
		ADDITIONAL_LABEL_WIDTH = additionalLabelWidth;
		buildingSlots = new List<>();
	}
	
	public void addBuildingSlot(int index, int x, int y) {
		buildingSlots.append(new BuildingSlot(BuildingSlotStatus.EMPTY, index, x, y));
	}
	
	public BuildingSlot getBuildingSlot(int index) {
		buildingSlots.toFirst();
		while (buildingSlots.hasAccess()) {
			if (index == buildingSlots.getContent().index)
				return buildingSlots.getContent();
			buildingSlots.next();
		}
		return null;
	}

	public class BuildingSlot {
		
		public final int index, x, y;
		private BuildingSlotStatus status;

		public BuildingSlot(BuildingSlotStatus status, int index, int x, int y) {
			this.status = status;
			this.index = index;
			this.x = x;
			this.y = y;
		}
		
		public void setStatus(BuildingSlotStatus status) {
			this.status = status;
		}
		
		public BuildingSlotStatus getStatus() {
			return status;
		}

	}

}
