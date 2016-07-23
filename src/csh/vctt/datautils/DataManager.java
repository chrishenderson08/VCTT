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

package csh.vctt.datautils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import csh.vctt.entities.ClassType;
import csh.vctt.entities.LineSkill;
import csh.vctt.entities.Player;
import csh.vctt.entities.Requirement;
import csh.vctt.entities.SkillWrapper;
import csh.vctt.entities.Tier;

/**
 * DataManager class is composed of methods to parse JSON data and query
 * the API.  This is also where constants are laid out to correspond to
 * queries and data members of JSON files.
 * @author Chris
 *
 */
public class DataManager{
	private final static String 	M_JSONMEM_CHARLIST = "character_list";
	private final static String		M_JSONMEM_CHAR_ID = "character_id";
	private final static String 	M_JSONMEM_CHAR_TOP_NAME = "name";
	private final static String 	M_JSONMEM_CHAR_TOP_BR = "battle_rank";
	private final static String 	M_JSONMEM_CHAR_TOP_CERTBAL = "certs";
	private final static String 	M_JSONMEM_CHAR_BR = "value";
	private final static String 	M_JSONMEM_CHAR_CERTBAL = "available_points";
	private final static String 	M_JSONMEM_CHAR_NAME = "first";
	
	private final static String 	M_JSONMEM_OUTF_MEMLIST = "outfit_member_list";
	private final static String 	M_JSONMEM_OUTF_TOP_CHAR = "character";
	private final static String 	M_JSONMEM_OUTF_TOP_CHAR_NAME = "name";
	private final static String 	M_JSONMEM_OUTF_CHAR_NAME = "first";
	private final static String 	M_JSONMEM_OUTF_CHAR_ID = "character_id";
	
	private final static String 	M_JSONMEM_TREE_TOP = "tiers";
	private final static String 	M_JSONMEM_TREE_TOP_CLASS = "classes";
	private final static String		M_JSONMEM_TREE_CLASS_NAME = "className";
	private final static String		M_JSONMEM_TREE_REQ_ID = "id";
	private final static String		M_JSONMEM_TREE_REQ_NAME = "name";
	private final static String		M_JSONMEM_TREE_REQ_COST = "cost";
	private final static String		M_JSONMEM_TREE_REQ_REQD = "reqd";
	private final static String		M_JSONMEM_TREE_REQ_ISITEM = "isItem";
	private final static String		M_JSONMEM_TREE_REQ_LINEID = "lineId";
	
	private final static String		M_JSONMEM_CERT_TOP_ITEM = "characters_item_list";
	private final static String		M_JSONMEM_CERT_TOP_SKILL= "characters_skill_list";
	private final static String		M_JSONMEM_CERT_ITEM_ID = "item_id";
	private final static String		M_JSONMEM_CERT_TOP_SKILL_LINE = "skill_id_join_skill";
	private final static String		M_JSONMEM_CERT_SKILL_ID = "skill_id";
	private final static String		M_JSONMEM_CERT_TOP_SKILL_NAME = "name";
	private final static String		M_JSONMEM_CERT_SKILL_NAME = "en";
	private final static String		M_JSONMEM_CERT_SKILL_LINE_ID = "skill_line_id";
	
	public final static String 		M_PLAYER_SELECT_QUERY = 
										"http://census.soe.com/get/ps2:v2/character/?c:show=name.first,battle_rank.value,certs.available_points,character_id&character_id=";
	public final static String 		M_OUTFITMEMBER_SELECT_QUERY =
										"http://census.soe.com/get/ps2:v2/outfit_member/?outfit_id=37509488620610014&c:resolve=character_name&c:show=character_id&c:limit=2000";
	public final static String		M_PLAYER_ITEM_SELECT_QUERY = 
										"http://census.soe.com/get/ps2:v2/characters_item/?c:limit=2000&character_id=";
	public final static String		M_PLAYER_SKILL_SELECT_QUERY =
										"http://census.soe.com/get/ps2:v2/characters_skill/?c:show=skill_id,skill_line_id&c:join=skill^show:skill_line_id&c:join=skill^show:name.en&c:limit=2000&character_id=";
	
	
	/**
	 * Returns a member of a JSON object as a String.
	 * @param obj JsonObject to be parsed into String.
	 * @param memberName The member's name in the JSON.
	 * @return Member's String value.
	 */
	private static String getMemberAsStr( JsonObject obj, String memberName ){
		JsonElement elem = obj.get( memberName );
		String elemStr = elem.toString();
		elemStr = elemStr.replace( "\"", "" );
		
		return elemStr;
	}
	
	/**
	 * Parses JSON into a Player Object.
	 * @param playerDatResults The JSON result of the player query to the API.
	 * @return Player object.
	 */
	public static Player getPlayerFromAPIResults( String playerDatResults ){
		Player player = null;
		
		JsonElement root = new JsonParser().parse( playerDatResults );
		JsonObject outMemListObj = root.getAsJsonObject();
		JsonArray outMemList = outMemListObj.getAsJsonArray( M_JSONMEM_CHARLIST );
		
		if( outMemList.size() != 0 ){
			JsonElement playerDatE = outMemList.get( 0 );
			JsonObject playerDat = playerDatE.getAsJsonObject();
			JsonObject nameObj = playerDat.getAsJsonObject( M_JSONMEM_CHAR_TOP_NAME );
			JsonObject brObj = playerDat.getAsJsonObject( M_JSONMEM_CHAR_TOP_BR );
			JsonObject certBalObj = playerDat.getAsJsonObject( M_JSONMEM_CHAR_TOP_CERTBAL );
			
			String id = getMemberAsStr( playerDat, M_JSONMEM_CHAR_ID );
			String name = getMemberAsStr( nameObj, M_JSONMEM_CHAR_NAME );
			String brStr = getMemberAsStr( brObj, M_JSONMEM_CHAR_BR );
			String certBalStr = getMemberAsStr( certBalObj, M_JSONMEM_CHAR_CERTBAL );
			
			int br = Integer.parseInt( brStr );
			int certBal = Integer.parseInt( certBalStr );
			
			if( id != null && !id.isEmpty() ){
				player = new Player( id, name, br, certBal );
			}else{
				System.err.println( "PLAYER INIT ERROR:  THERE WAS AN ERROR PARSING THE PLAYER DATA." );
			}
		}else{
			System.err.println( "PLAYER INIT ERROR:  THIS PLAYER DATA CORRUPTED OR INCORRECT." );
		}
		
		return player;
	}
	
	/**
	 * Parses JSON into list containing the unique identifiers of certed items.
	 * @param itemDat The JSON result of the item query to the API.
	 * @return ArrayList<String> listing the items certed into.
	 */
	public static ArrayList<String> getItemsFromAPIResults( String itemDat ){
		ArrayList<String> certs = new ArrayList<String>();
		
		JsonElement root = new JsonParser().parse( itemDat );
		JsonObject rootObj = root.getAsJsonObject();
		
		JsonArray certList = null;
		if( rootObj.has( M_JSONMEM_CERT_TOP_ITEM ) ){
			certList = rootObj.getAsJsonArray( M_JSONMEM_CERT_TOP_ITEM );
		}else{
			System.err.println( "CERT PARSE ERROR:  INVALID JSON" );
		}
		
		if( certList != null ){ 
			for(JsonElement curCertElem : certList){
				JsonObject curCertObj = curCertElem.getAsJsonObject();
				certs.add( getMemberAsStr( curCertObj, M_JSONMEM_CERT_ITEM_ID ) );
			}
		}
		
		return certs;
	}
	
	/**
	 * Parses JSON into a SkillWrapper containing info about certed skills.
	 * @param skillDat The JSON result of the skill query to the API.
	 * @return SkillWrapper containing skill completion data.
	 */
	public static SkillWrapper getSkillsFromAPIResults( String skillDat ){
		SkillWrapper skills = new SkillWrapper();
		
		JsonElement root = new JsonParser().parse( skillDat );
		JsonObject rootObj = root.getAsJsonObject();
		
		JsonArray certList = null;
		if( rootObj.has( M_JSONMEM_CERT_TOP_SKILL ) ){
			certList = rootObj.getAsJsonArray( M_JSONMEM_CERT_TOP_SKILL );
		}else{
			System.err.println( "CERT PARSE ERROR:  INVALID JSON" );
		}
		
		if( certList != null ){
			for(JsonElement curCertElem : certList){
				JsonObject curCertObj = curCertElem.getAsJsonObject();
				JsonObject skillDatObj = curCertObj.getAsJsonObject( M_JSONMEM_CERT_TOP_SKILL_LINE );
				JsonObject nameObj = skillDatObj.getAsJsonObject( M_JSONMEM_CERT_TOP_SKILL_NAME );
				
				String id = getMemberAsStr( curCertObj, M_JSONMEM_CERT_SKILL_ID );
				String lineId = getMemberAsStr( skillDatObj, M_JSONMEM_CERT_SKILL_LINE_ID );
				String name = getMemberAsStr( nameObj, M_JSONMEM_CERT_SKILL_NAME );
				
				//Check if this skill is a line skill
				String lastChar = name.substring(name.length() - 1);
				boolean isInt = true;
				try {
					Integer.parseInt( lastChar ); 
				}catch( NumberFormatException e ){ 
					isInt = false;
				}
				
				if(isInt){
					LineSkill lSkill = new LineSkill( id, lineId, name );
					lSkill.getId();
					skills.addLineSkill( lSkill );
				}else{
					skills.addBasicSkill( id );
				}
			}
		}
		
		return skills;
	}
	
	/**
	 * Parses CertTree.json file into Tiers.
	 * @param certTreeFile The contents of the cert tree file.
	 * @return ArrayList containing all of the tiers in the tree.
	 */
	public static ArrayList<Tier> getTreeFromFile( String certTreeFile ){
		ArrayList<Tier> tiers = new ArrayList<Tier>();
		
		JsonElement root = new JsonParser().parse( certTreeFile );
		JsonObject topObj = root.getAsJsonObject();
		JsonArray topArr = topObj.getAsJsonArray( M_JSONMEM_TREE_TOP );
		
		JsonElement tierTop = topArr.get( 0 );
		JsonObject tierTopObj = tierTop.getAsJsonObject();
		
		int tierCt = 1;
		while( tierTopObj.has( "tier" + tierCt ) ){
			String tierName = "tier" + tierCt;
			Tier curTier = new Tier( tierName );
			tiers.add( curTier );
			
			JsonArray curTierArr = tierTopObj.getAsJsonArray( tierName );
			
			JsonElement classTop = curTierArr.get( 0 );
			JsonObject classTopObject = classTop.getAsJsonObject();
			JsonArray classArr = classTopObject.getAsJsonArray( M_JSONMEM_TREE_TOP_CLASS );
			for( JsonElement curClassElem : classArr ){
				JsonObject curClassTopObj = curClassElem.getAsJsonObject();
				String className = getMemberAsStr( curClassTopObj, M_JSONMEM_TREE_CLASS_NAME );
				
				ClassType curClass = new ClassType( className );
				curTier.addClass( curClass );
				
				JsonArray curClassArr = curClassTopObj.getAsJsonArray( className );
				
				for( JsonElement curReqElem : curClassArr ){
					JsonObject curReqObj = curReqElem.getAsJsonObject();
					String reqId = getMemberAsStr( curReqObj, M_JSONMEM_TREE_REQ_ID );
					String reqName = getMemberAsStr( curReqObj, M_JSONMEM_TREE_REQ_NAME );
					String reqCost = getMemberAsStr( curReqObj, M_JSONMEM_TREE_REQ_COST );
					String reqReqd = getMemberAsStr( curReqObj, M_JSONMEM_TREE_REQ_REQD );
					String reqIsItem = getMemberAsStr( curReqObj, M_JSONMEM_TREE_REQ_ISITEM );
					String reqLineId = getMemberAsStr( curReqObj, M_JSONMEM_TREE_REQ_LINEID );
					
					Requirement curReq = new Requirement( reqId, reqName, reqCost, reqReqd, reqIsItem, reqLineId );
					curClass.addReq( curReq );
				}
			}
			
			tierCt++;
		}
		
		return tiers;
	}
	
	/**
	 * Parses JSON into Players.
	 * @param outfitMembersJson The JSON result of the outfit member query to the API.
	 * @return ArrayList containing Player objects for all outfit members.
	 */
	public static ArrayList<Player> getOutfitMembers( String outfitMembersJson ){
		ArrayList<Player> outfitMembers = new ArrayList<Player>();
		
		JsonElement root = new JsonParser().parse( outfitMembersJson );
		JsonObject obj = root.getAsJsonObject();
		JsonArray outfitMemArr = obj.getAsJsonArray( M_JSONMEM_OUTF_MEMLIST );
		
		for( JsonElement curCharE : outfitMemArr ){
			JsonObject curChar = curCharE.getAsJsonObject();
			
			if( curChar.has( M_JSONMEM_OUTF_TOP_CHAR )){
				JsonObject charObj = curChar.getAsJsonObject( M_JSONMEM_OUTF_TOP_CHAR );
				
				JsonObject charNameObj = charObj.getAsJsonObject( M_JSONMEM_OUTF_TOP_CHAR_NAME );
				String id = getMemberAsStr( curChar, M_JSONMEM_OUTF_CHAR_ID );
				String name = getMemberAsStr( charNameObj, M_JSONMEM_OUTF_CHAR_NAME );
					
				outfitMembers.add( new Player( id, name ) );
			}
		}
		
		Collections.sort( outfitMembers );
		
		return outfitMembers;
	}
	
	/**
	 * Executes a query to the API.
	 * @param query String value of the query.
	 * @return JSON result of the query.
	 */
	public static String execQuery( String query ){
		String queryResult = null;
		
		URL queryURL = null;
		try{
			queryURL = new URL( query );
		}catch ( MalformedURLException e ){
			System.err.println( "API ERROR: BAD PROTOCOL GIVEN IN QUERY." );
			e.printStackTrace();
		} 
		
		InputStream queryStream = null;
		if( queryURL != null ){
			try{
				queryStream = queryURL.openStream();
			}catch ( IOException e ){
				System.err.println( "API ERROR: I/O ERROR AT CREATION OF RESULTS INPUT STREAM." );
				e.printStackTrace();
			}
		}
		
		if( queryStream != null ){
			BufferedReader inputReader = new BufferedReader( new InputStreamReader( queryStream ) );
			StringBuffer webData = new StringBuffer();
			
			String inputLine = null;
			try{
				while ( ( inputLine = inputReader.readLine() ) != null ){
					webData.append( inputLine );
					webData.append( "\n" );
				}
				inputReader.close();
			}catch ( IOException e ){
				System.err.println( "API ERROR: I/O ERROR AT CREATION OF QUERY OUTPUT FILE" );
				e.printStackTrace();
			}
			
			queryResult = webData.toString();
		}

		return queryResult;
	}
}
