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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Tier as pulled from the CertTree.json file.  Note that a Tier
 * object contains all of its subordinate classes.  This is the top-level
 * cert tree object.
 * @author Chris
 *
 */
public class Tier{
	private String 				m_tierName;
	private List<ClassType> 	m_classes;
	
	public Tier( String tierName ){
		m_tierName = tierName;
		m_classes = new ArrayList<ClassType>();
	}
	
	public String getName(){ return m_tierName; }
	public List<ClassType> getClasses(){ return m_classes; }
	public void addClass(ClassType c){ m_classes.add( c ); }
	
	@Override public String toString(){ return m_tierName; }
}
