/**   Copyright 2013, Chris Henderson
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

package csh.vctt.entities;

/**
 * Contains relevant information about a character.
 * @author Chris
 *
 */
public class Player implements Comparable<Player>{
	private String 		m_playerId;
	private String 		m_playerName;
	private int 		m_battleRank;
	private int 		m_certPtBal;
	
	public Player( String playerId, String playerName, int battleRank, int certPtBal ){
		m_playerId = playerId;
		m_playerName = playerName;
		m_battleRank = battleRank;
		m_certPtBal = certPtBal;
	}
	
	public Player( String playerId, String playerName ){
		m_playerId = playerId;
		m_playerName = playerName;
		m_battleRank = -1;
		m_certPtBal = -1;
	}
	
	public String getId(){ return m_playerId; }
	public String getName(){ return m_playerName; }
	public int getBR(){ return m_battleRank; }
	public int getCertBal(){ return m_certPtBal; }
	
	public void setBR( int br ){ m_battleRank = br; }
	public void setCertBal( int certPtBal ){ m_certPtBal = certPtBal; }
	
	@Override
	public String toString(){ return m_playerName; }

	@Override
	public int compareTo( Player o ){
		int returnVal = 0;
		
		String nameLow = m_playerName.toLowerCase();
		String oName = o.getName();
		String oNameLow = oName.toLowerCase();
		
		if( nameLow.compareTo( oNameLow ) > 0 ){
			returnVal = 1;
		}else if( nameLow.compareTo( oNameLow ) < 0 ){
			returnVal = -1;
		}else{
			returnVal = 0;
		}
		
		return returnVal;
	}
}
