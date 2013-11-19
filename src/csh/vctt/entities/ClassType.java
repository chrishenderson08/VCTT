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
 * ClassType represents a certification class like the Heavy Assault, Medic,
 * Sunderer, etc.  These are pulled from the CertTree.json file.  Note that
 * a ClassType object contains all of its subordinate requirements.
 * @author Chris
 *
 */
public class ClassType{
	private String 					m_className;
	private List<Requirement> 		m_reqs;
	
	public ClassType( String className ){
		m_className = className;
		m_reqs = new ArrayList<Requirement>();
	}
	
	public String getName(){ return m_className; }
	public List<Requirement> getReqs(){ return m_reqs; }
	public void addReq( Requirement r ){ m_reqs.add(r); }
	
	@Override public String toString(){ return m_className; }
}