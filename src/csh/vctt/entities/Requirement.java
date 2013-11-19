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
 * Represents a particular certification requirement pulled 
 * from the CertTree.json file.
 * @author Chris
 *
 */
public class Requirement{
	private String 		m_reqId;
	private String 		m_reqName;
	private String 		m_cost;
	private String 		m_isRequired;
	private String 		m_isItem;
	private String		m_lineId;
	
	public Requirement( String reqId, String reqName, String cost, String isRequired, String isItem, String lineId ){
		m_reqId = reqId;
		m_reqName = reqName;
		m_cost = cost;
		m_isRequired = isRequired;
		m_isItem = isItem;
		m_lineId = lineId;
	}
	
	public String getId(){ return m_reqId; }
	public String getName(){ return m_reqName; }
	public String getCost(){ return m_cost; }
	public String getRequired(){ return m_isRequired; }
	public String getItem(){ return m_isItem; }
	public String getLineId(){ return m_lineId; }
}
