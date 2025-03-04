/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2021 ImageJ2 developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
import ij.IJ;
import ij.Menus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;


public class Submenu_Test implements PlugIn {

	@Override
	public void run(String arg) {
		final GenericDialog gd = new GenericDialog("Submenu Test");
		gd.addStringField("menupath", "");
		gd.addStringField("class", "");
		gd.showDialog();
		if (gd.wasCanceled()) return;

		final String menuPath = gd.getNextString();
		final String className = gd.getNextString();

		final String label = findMenuItem(menuPath);
		final String commandClass = (String) Menus.getCommands().get(label);
		if (!className.equals(commandClass)) {
			throw new RuntimeException("Unexpected command: " + commandClass);
		}
	}

	private String findMenuItem(String menuPath) {
		final String[] list = menuPath.split(">");
		MenuItem menu = null;
		outer:
		for (int i = 0; i < list.length; i++) {
			if (i == 0) {
				final MenuBar bar = IJ.getInstance().getMenuBar();
				for (int j = 0; j < bar.getMenuCount(); j++) {
					final Menu candidate = bar.getMenu(j);
					if (list[i].equals(candidate.getLabel())) {
						menu = candidate;
						continue outer;
					}
				}
				throw new RuntimeException("Top-level menu " + list[i] + " not found!"); 
			}
			for (int j = 0; j < ((Menu) menu).getItemCount(); j++) {
				final MenuItem candidate = ((Menu) menu).getItem(j);
				if (list[i].equals(candidate.getLabel())) {
					menu = candidate;
					continue outer;
				}
			}
			throw new RuntimeException("Submenu " + list[i] + " not found!");
		}
		return menu.getLabel();
	}

}
