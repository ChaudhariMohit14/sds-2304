package com.smartdatasolutions.test.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smartdatasolutions.test.Member;
import com.smartdatasolutions.test.MemberExporter;
import com.smartdatasolutions.test.MemberFileConverter;
import com.smartdatasolutions.test.MemberImporter;

public class Main extends MemberFileConverter {

	@Override
	protected MemberExporter getMemberExporter( ) {
		// TODO
		return new MemberExporterImpl();
	}

	@Override
	protected MemberImporter getMemberImporter( ) {
		// TODO
		return new MemberImporterImpl();
	}

	@Override
	protected List< Member > getNonDuplicateMembers( List< Member > membersFromFile ) {

		// TODO
		Set<String> membersID = new HashSet<>();
		List<Member> nonDuplicateMembers = new ArrayList<>();
		
		for(Member member: membersFromFile) {
			if(!membersID.contains(member.getId())) {
				membersID.add(member.getId());
				nonDuplicateMembers.add(member);
			}
		}
		return nonDuplicateMembers;
	}

	@Override
	protected Map< String, List< Member >> splitMembersByState( List< Member > validMembers ) {
		// TODO
		Map< String, List< Member >> membersByState = new HashMap<>();
		
		for(Member member: validMembers) {
			String state = member.getState();
			List<Member> stateMembers = membersByState.getOrDefault(state, new ArrayList<>());
			
			stateMembers.add(member);
			membersByState.put(state, stateMembers);
		}
		return membersByState;
	}

	public static void main( String[] args ) {
		//TODO
		
		Main main = new Main();
		
		try {
			List<Member> members = main.getMemberImporter().importMembers(new File("Members.txt"));
			List<Member> nonDuplicateMembers = main.getNonDuplicateMembers(members);
			Map<String, List<Member>> membersByState = main.splitMembersByState(nonDuplicateMembers);
			
			for(Map.Entry<String, List<Member>> map : membersByState.entrySet()) {
				String state = map.getKey();
				List<Member> stateMembers = map.getValue();
				
				String outputFileName= state+"_output.csv";
				try(FileWriter fw = new FileWriter(outputFileName)){
					MemberExporter memberExporter = main.getMemberExporter();
					for(Member member : stateMembers) {
						memberExporter.writeMember(member, fw);
						fw.write("\n");
					}
				} catch(IOException e){
					System.out.println(e.getMessage());
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
