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

import java.util.ArrayList;
import java.util.List;

/**
 * SkillWrapper is a data construct that contains all of the basic
 * skills that a character has certed into, as well as necessary data
 * on the LineSkills they have completed.  This was created out of the
 * necessity realized after I saw that skills are done a bit differently
 * in some cases with respect to the API data.
 * @author Chris
 *
 */
public class SkillWrapper {
	private List<String> 		m_basicSkillIds;
	private List<LineSkill>		m_lineSkills;
	
	public SkillWrapper(){
		m_basicSkillIds = new ArrayList<String>();
		m_lineSkills = new ArrayList<LineSkill>();
	}
	
	public List<String> getBasicSkills(){ return m_basicSkillIds; }
	public List<LineSkill> getLineSkills(){ return m_lineSkills; }
	public void addBasicSkill( String id ){ m_basicSkillIds.add( id ); }
	public void addLineSkill( LineSkill lSkill ){ m_lineSkills.add( lSkill ); }
	
	/**
	 * Checks if a basic skill is completed.
	 * @param id Unique id number of the basic skill being checked.
	 * @return Is the skill certed into.
	 */
	public boolean checkBasicCompletion( String id ){ return m_basicSkillIds.contains( id ); }
	
	/**
	 * Checks is a numbered skill in a skill line is completed.
	 * @param name Name of the skill.
	 * @param lineId Unique id number of the skill line.
	 * @return Is the skill certed into.
	 */
	public boolean checkLineCompletion( String name, String lineId ){
		String lvlStr = name.substring( name.length() - 1 );
		int curLvl = Integer.parseInt( lvlStr );
		int highLvl = getHighLineLvl( lineId );
		
		return ( highLvl >= curLvl );
	}
	
	/**
	 * Returns highest level certed into of the given skill line.
	 * @param lineId Uniqe id number of the skill line.
	 * @return
	 */
	public int getHighLineLvl( String lineId ){
		int highLvl = -1;
		
		ArrayList<Integer> lineSkills = new ArrayList<Integer>();
		for( LineSkill curLineSk : m_lineSkills ){
			String curLineId = curLineSk.getLineId();
			if( curLineId.equals( lineId ) ){
				lineSkills.add( curLineSk.getLevel() );
			}
		}
		
		for( int curLvl : lineSkills ){
			if( curLvl > highLvl ){
				highLvl = curLvl;
			}
		}
		
		return highLvl;
	}
}
