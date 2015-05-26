package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiWrapper;

public class BackCmd implements Command {

	GuiState state;
	GuiText text;
	GuiText text2;
	
	public BackCmd(GuiState state) {
		this.state = state;
	}

	@Override
	public void execute() {
		GuiWrapper.getInstance().setCurrentState(state.getPrevState().getName());		
		if (text != null) {
			text.setTransparency(1f);
		}
		if (text2 != null) {
			text2.setTransparency(1f);
		}
	}
	
	public void setNoteText(GuiText resNoteText) {
		text = resNoteText;
	}
	
	public void setNoteText2(GuiText resNoteText) {
		text2 = resNoteText;
	}
}
