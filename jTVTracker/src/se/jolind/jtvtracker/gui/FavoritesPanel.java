package se.jolind.jtvtracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.data.tvmaze.TvmShortShow;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class FavoritesPanel extends JPanel {
	
		private JPanel resultsPanel;
		private JLabel lblResultsInfo;
		private JScrollPane scrollPane;
		private List<Show> favShows;
		private GridBagConstraints gc;
		private IShowChange showListener;
		
		public FavoritesPanel () {
						
			resultsPanel = new JPanel();
			resultsPanel.setLayout(new GridBagLayout());
			gc = new GridBagConstraints();
			
			scrollPane = new JScrollPane();
			scrollPane.getViewport().add(resultsPanel);
			setLayout(new BorderLayout());
				
			add(scrollPane,BorderLayout.CENTER);
		}

		public void updateInfo(List<Show> favShows){
			
			this.favShows = favShows;
			lblResultsInfo = new JLabel();
			lblResultsInfo.setText(" ");
			scrollPane.getViewport().add(resultsPanel);
									
			JLabel lblFiller = new JLabel(" ");
			
			JLabel[] searchName = new JLabel[favShows.size()];
			JLabel[] searchPic = new JLabel[favShows.size()];

			int gridPos = 0;

			for (int i = 0; i < favShows.size(); i++) {

				Show currResult = favShows.get(i);

				int id = currResult.getId();
				searchName[i] = new JLabel(currResult.getInfo());
				searchPic[i] = new JLabel(currResult.getIconSmall());

				searchName[i].setOpaque(true);
				searchPic[i].setOpaque(true);

				Color normal = searchName[i].getBackground();
				JLabel currLabel = searchName[i];
				JLabel currPicLabel = searchPic[i];

				searchName[i].addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent e) {
						currLabel.setBackground(normal);
						currPicLabel.setBackground(normal);

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						currLabel.setBackground(Color.WHITE);
						currPicLabel.setBackground(Color.WHITE);
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						if (showListener == null) {
							showListener = Controller.getListener();
						}
						showListener.showChangedEvent(id);

					}
				});

				gc.gridx = 0;
				gc.gridy = gridPos;
				gc.weightx = 0.1;
				gc.weighty = 0.1;
				gc.gridwidth = 1;
				gc.insets = new Insets(0, 0, 0, 0);
				gc.fill = GridBagConstraints.BOTH;
				gc.anchor = GridBagConstraints.LINE_START;
				resultsPanel.add(searchName[i], gc);

				gc.gridx = 1;
				gc.weightx = 0.1;
				gc.weighty = 0.1;
				gc.insets = new Insets(0, 0, 0, 0);
				gc.fill = GridBagConstraints.BOTH;
				gc.anchor = GridBagConstraints.LINE_END;
				resultsPanel.add(searchPic[i], gc);

				gridPos++;
			}

			gc.gridx = 0;
			gc.gridy = gridPos;
			gc.weightx = 0.1;
			gc.weighty = 1;
			gc.gridwidth = 1;
			gc.insets = new Insets(0, 0, 0, 0);
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.LINE_START;
			resultsPanel.add(lblFiller, gc);
		}
}
