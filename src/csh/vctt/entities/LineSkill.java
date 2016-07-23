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

package csh.vctt.entities;

/**
 * LineSkill represents a numbered skill and its respective skill
 * line (e.g. - Medical Applicator 1, Medical Applicator 2......)
 * @author Chris
 *
 */
public class LineSkill {
	private String		m_skillId;
	private String		m_lineId;
	private String		m_skillName;
	private int			m_skillLevel;
	
	public LineSkill( String skillId, String lineId, String skillName ){
		m_skillId = skillId;
		m_lineId = lineId;
		m_skillName = skillName;
		m_skillLevel = -1;
		
		String lvlStr = skillName.substring( skillName.length() - 1 );
		
		try { 
	        m_skillLevel = Integer.parseInt( lvlStr ); 
	    }catch( NumberFormatException e ){ 
	         System.err.println( "ERROR: NON-LINE SKILL PASSED TO LINESKILL CONSTRUCTOR." );
	    }
	}
	
	public String getId(){ return m_skillId; }
	public String getLineId(){ return m_lineId; }
	public String getName(){ return m_skillName; }
	public int getLevel(){ return m_skillLevel; }
}
