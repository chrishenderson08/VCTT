/**   Copyright 2013, see AUTHORS file.
***
*** Licensed under the Apache License, Version 2.0 (the "License");
*** you may not use this file except in compliance with the License.
*** You may obtain a copy of the License at
***
***     http://www.apache.org/licenses/LICENSE-2.0
***
*** Unless required by applicable law or agreed to in writing, software
*** distributed under the License is distributed on an "AS IS" BASIS,
*** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*** See the License for the specific language governing permissions and
*** limitations under the License.
***/

package csh.vctt.launcher;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.sun.corba.se.impl.util.Utility;

import csh.vctt.datautils.DataManager;
import csh.vctt.entities.Player;
import csh.vctt.entities.SkillWrapper;
import csh.vctt.entities.Tier;
import csh.vctt.gui.CertTreeGui;

/**
 * The bootstrapping class that initializes all components and reads/queries
 * necessary input for the application to run.
 * @author Chris
 *
 */
public class Launcher{	
	private File 					m_playerDatFile;
	private InputStream				m_certTreeFile;
	private List<Tier> 				m_tiers;
	
	private Player 					m_player;
	
	private final static String		M_GUI_TITLE = "Voodoo Company Cert Tree Tracker - ";
	
	public Launcher(){
		String userDir = System.getProperty( "user.home" );
		
		m_playerDatFile = new File( userDir + "\\AppData\\Roaming\\VCTT\\PlayerDat.txt" );
		m_certTreeFile = Utility.class.getResourceAsStream( "/config/CertTree.json" );
		m_tiers = new ArrayList<Tier>();
	}
	
	/**
	 * Initialize the Player object by querying the API with the
	 * unique character ID number and parsing the results.
	 */
	private void initPlayer(){		
		String playerId = "";
		
		try{
			playerId = FileUtils.readFileToString( m_playerDatFile );
		}catch ( IOException e ){
			System.err.println( "ERROR:  IOException.  ENSURE PlayerDat.txt FILE EXISTS" );
			e.printStackTrace();
		}
		if( !playerId.isEmpty() ){
			String playerDat = DataManager.execQuery( DataManager.M_PLAYER_SELECT_QUERY + playerId );
			m_player = DataManager.getPlayerFromAPIResults( playerDat );
		}
	}
	
	/**
	 * Initialize the cert tree structure by parsing the
	 * CertTree.json config file.
	 */
	private void initCertTree(){
		StringWriter treeFileWriter = new StringWriter();
		
		try{
			IOUtils.copy( m_certTreeFile, treeFileWriter );
		}catch ( IOException e ){
			System.err.println( "ERROR:  IOException.  ENSURE CertTree.json FILE EXISTS" );
			e.printStackTrace();
		}
		
		String treeFile = treeFileWriter.toString();
		
		if( !treeFile.isEmpty() ){
			m_tiers = DataManager.getTreeFromFile( treeFile );
		}
	}
	
	/**
	 * Prompt the user to select their character from a dialog.
	 * @return Unique id number of the selected character.
	 */
	private String getPlayerSelection(){
		String selectedId = "-1";
		
		String outfitMembersJson = DataManager.execQuery( DataManager.M_OUTFITMEMBER_SELECT_QUERY );
		List<Player> outfitMembers = DataManager.getOutfitMembers( outfitMembersJson );
		Object[] outfitMembersArrObj = outfitMembers.toArray();
		Player[] outfitMembersArr = new Player[outfitMembersArrObj.length];
		for( int i = 0; i < outfitMembersArrObj.length; i++ ){
			outfitMembersArr[i] = (Player)outfitMembersArrObj[i];
		}
		
		JList<Player> memSelList = new JList<Player>( outfitMembersArr );
		memSelList.setVisibleRowCount( 10 );
		
		JScrollPane memScrollPane = new JScrollPane( memSelList );
		memScrollPane.setPreferredSize( new Dimension( 200, 400 ) );
		
		int userAction = JOptionPane.showConfirmDialog( null, memScrollPane, "Select Character and Press Ok", JOptionPane.PLAIN_MESSAGE );
		
		Player selectedPlayer = null;
		if( userAction == JOptionPane.YES_OPTION ){
			selectedPlayer = memSelList.getSelectedValue();
		}
		
		if( selectedPlayer != null ){
			selectedId = selectedPlayer.getId();
		}
		
		return selectedId;
	}
	
	/**
	 * Write out the given id to the PlayerDat.txt file.
	 * @param id Unique character id.
	 */
	private void createPlayerDatFile( String id ){
		try{
			FileUtils.writeStringToFile( m_playerDatFile, id );
		}catch ( IOException e ){
			System.err.println( "ERROR:  IOException.  ERROR WRITING PlayerDat.txt FILE." );
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the Launcher.  Checks if user has selected a character
	 * for the application to track.
	 */
	public void init(){
		if( m_playerDatFile.exists() ){
			initPlayer();
		}else{
			String playerId = getPlayerSelection();
			if( !playerId.equals( "-1" ) ){
				createPlayerDatFile( playerId );
				initPlayer();
			}
		}
		
		if( m_certTreeFile != null ){
			initCertTree();
		}else{
			System.err.println( "ERROR: CERT TREE CONFIG FILE NOT FOUND!" );
		}
	}
	
	/**
	 * Initialize and display the GUI.
	 */
	public void createFrame(){
		String charItemResult = DataManager.execQuery( DataManager.M_PLAYER_ITEM_SELECT_QUERY + m_player.getId() );
		ArrayList<String> items = DataManager.getItemsFromAPIResults( charItemResult );
		
		String charSkillResult = DataManager.execQuery( DataManager.M_PLAYER_SKILL_SELECT_QUERY + m_player.getId() );
		SkillWrapper skills = DataManager.getSkillsFromAPIResults( charSkillResult );
		
		CertTreeGui gui = new CertTreeGui( M_GUI_TITLE + m_player.getName(), m_tiers, m_player, items, skills );
		
		gui.initControls();
		gui.initCertTreeTable();
		gui.initInfoArea();
		gui.setVisible(true);
	}
}
