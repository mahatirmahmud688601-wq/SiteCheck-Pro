package com.example.data.demo

import com.example.data.model.*

object DemoDataProvider {
    fun getInitialProjects(): List<ProjectEntity> = listOf(
        ProjectEntity(
            id = 1,
            name = "Morgan Industrial Park",
            projectCode = "MORGAN_Jan17 -",
            clientName = "Granville Property Group",
            location = "East Logistics Hub, Units 1-14",
            status = "Active",
            creationDate = "12/01/2026",
            description = "Comprehensive quarterly site safety, hazard identification, and snagging audit.",
            totalInspections = 5
        ),
        ProjectEntity(
            id = 2,
            name = "Sussex Gardens",
            projectCode = "PSG10 -",
            clientName = "Sussex County Council",
            location = "Phase 2 Green Infrastructure Sector",
            status = "Active",
            creationDate = "10/01/2026",
            description = "Public walkway pavement and perimeter barrier audit.",
            totalInspections = 3
        ),
        ProjectEntity(
            id = 3,
            name = "26 Hall Drive",
            projectCode = "HD26006 -",
            clientName = "Hallfield Residential Trust",
            location = "Block C Residential Estate",
            status = "Active",
            creationDate = "08/01/2026",
            description = "Structural snagging and balcony drainage inspection.",
            totalInspections = 2
        ),
        ProjectEntity(
            id = 4,
            name = "Bromley High Street",
            projectCode = "BHSP50 -",
            clientName = "Bromley Urban Development",
            location = "Commercial Pedestrian Walkway",
            status = "Active",
            creationDate = "06/01/2026",
            description = "Retail facade reinforcement and utility covers.",
            totalInspections = 4
        ),
        ProjectEntity(
            id = 5,
            name = "91 Lockyer Place",
            projectCode = "LP911001 -",
            clientName = "Lockyer Logistics Depot",
            location = "Warehouse 5 Loading Bays",
            status = "Active",
            creationDate = "04/01/2026",
            description = "Heavy machinery clearance and fire safety lanes.",
            totalInspections = 2
        ),
        ProjectEntity(
            id = 6,
            name = "31 Baker Street",
            projectCode = "P31BS -",
            clientName = "Baker St Developments",
            location = "Floors 2-6 Commercial Office",
            status = "Active",
            creationDate = "03/01/2026",
            description = "HVAC ducts, fire dampers, and emergency stairwells.",
            totalInspections = 3
        ),
        ProjectEntity(
            id = 7,
            name = "19 Penry Street",
            projectCode = "PENSSTP19 -",
            clientName = "Penry Construction Co",
            location = "Substructure Foundations",
            status = "Completed",
            creationDate = "19/12/2025",
            description = "Pre-pour concrete rebar and waterproofing signoff.",
            totalInspections = 6
        ),
        ProjectEntity(
            id = 8,
            name = "15 York Street",
            projectCode = "YS15004 -",
            clientName = "York Heritage Management",
            location = "Historical Masonry Facade",
            status = "Active",
            creationDate = "14/12/2025",
            description = "Scaffolding anchoring and brick spalling review.",
            totalInspections = 4
        ),
        ProjectEntity(
            id = 9,
            name = "34 Digby Cresent",
            projectCode = "34DG009 -",
            clientName = "Digby Warehousing UK",
            location = "Depot Terminal Yard",
            status = "Active",
            creationDate = "13/12/2025",
            description = "Drainage retention pond and perimeter fencing.",
            totalInspections = 1
        )
    )

    fun getInitialIssues(): List<IssueEntity> = listOf(
        IssueEntity(
            id = 1,
            projectId = 1,
            title = "General Waste - Near Exit 2",
            category = "Housekeeping & Waste",
            severity = "Critical",
            status = "Open",
            assignedTo = "James Miller",
            locationDetail = "Near Exit 2 Industrial Gate",
            description = "There is a large pile of general waste located near exit 2 of the Industrial Estate. This needs to be cleared as soon as possible to prevent pest infestation and blocked emergency vehicular egress.",
            costEstimate = "£320 + VAT",
            photoRes = "waste",
            dateCreated = "Today, 08:30 AM"
        ),
        IssueEntity(
            id = 2,
            projectId = 1,
            title = "Blocked Drain",
            category = "Plumbing & Drainage",
            severity = "High",
            status = "Open",
            assignedTo = "Sarah Perry",
            locationDetail = "Next to Garage 1",
            description = "The drain is blocked with heavy silt and runoff causing stagnant water and a foul smell. Please contact sewage and pressure jetting contractor by Monday.",
            costEstimate = "£250",
            photoRes = "drain",
            dateCreated = "Today, 09:15 AM"
        ),
        IssueEntity(
            id = 3,
            projectId = 1,
            title = "Concrete Damage",
            category = "Structural",
            severity = "High",
            status = "In Progress",
            assignedTo = "Frank Vaughn",
            locationDetail = "Car Park Access Bays",
            description = "There is concrete damage in many different areas of the pavement surrounding the car park. These concrete slabs will need to be replaced before forklift operations resume.",
            costEstimate = "£580 + VAT",
            photoRes = "concrete",
            dateCreated = "Yesterday, 04:20 PM"
        ),
        IssueEntity(
            id = 4,
            projectId = 1,
            title = "Industrial Waste - Garage 4",
            category = "Housekeeping & Waste",
            severity = "Medium",
            status = "Open",
            assignedTo = "Jessica Gardner",
            locationDetail = "Garage 4 Exterior Apron",
            description = "Old chemical drums and scrap pallets stacked against building cladding without weather tarp.",
            costEstimate = "£450 + VAT",
            photoRes = "industrial_waste",
            dateCreated = "Yesterday, 02:10 PM"
        ),
        IssueEntity(
            id = 5,
            projectId = 1,
            title = "Spilt Paint",
            category = "Safety & Hazard",
            severity = "Low",
            status = "Resolved",
            assignedTo = "Scott Cook",
            locationDetail = "Walkway Pathway A",
            description = "Yellow floor marking paint spill dried on pedestrian walkway. Pressure washed and surface restored.",
            costEstimate = "£140",
            photoRes = "paint_spill",
            dateCreated = "05/01/2026"
        ),
        IssueEntity(
            id = 6,
            projectId = 1,
            title = "Worn Painted Areas",
            category = "Safety & Hazard",
            severity = "Medium",
            status = "In Progress",
            assignedTo = "Matthew Murphy",
            locationDetail = "Loading Bay 3 Edge",
            description = "Safety boundary caution lines have rubbed off due to heavy forklift tire traction.",
            costEstimate = "£300",
            photoRes = "worn_paint",
            dateCreated = "04/01/2026"
        ),
        IssueEntity(
            id = 7,
            projectId = 1,
            title = "Chipped Brick - North Entrance",
            category = "Structural",
            severity = "Medium",
            status = "Open",
            assignedTo = "Carlos Medina",
            locationDetail = "North Entrance Column",
            description = "Vehicle impact has dislodged facing bricks near ground level corner.",
            costEstimate = "£350 + VAT",
            photoRes = "brick",
            dateCreated = "03/01/2026"
        ),
        IssueEntity(
            id = 8,
            projectId = 1,
            title = "Missing Road Sign",
            category = "Safety & Hazard",
            severity = "High",
            status = "Open",
            assignedTo = "General Manager",
            locationDetail = "Main Junction Access Bend",
            description = "5 MPH Speed limit and stop sign knocked down during delivery maneuvers.",
            costEstimate = "£180",
            photoRes = "road_sign",
            dateCreated = "02/01/2026"
        )
    )

    fun getInitialTeam(): List<TeamMemberEntity> = listOf(
        TeamMemberEntity(1, "John Smith", "Lead Field Auditor", "john.smith@granvilleaudit.co", "+44 7700 900123", 2),
        TeamMemberEntity(2, "James Miller", "Site Operations Manager", "j.miller@sitecheck.com", "+44 7700 900456", 3),
        TeamMemberEntity(3, "Sarah Perry", "Drainage & Facilities Lead", "s.perry@facilities.co.uk", "+44 7700 900789", 2),
        TeamMemberEntity(4, "Frank Vaughn", "Senior Civil Engineer", "f.vaughn@granvilleaudit.co", "+44 7700 900321", 1),
        TeamMemberEntity(5, "Jessica Gardner", "Environmental & Waste Officer", "j.gardner@greenfield.org", "+44 7700 900654", 1),
        TeamMemberEntity(6, "Scott Cook", "HSE Safety Officer", "s.cook@granvilleaudit.co", "+44 7700 900987", 0),
        TeamMemberEntity(7, "Matthew Murphy", "Civil Maintenance Tech", "m.murphy@sitecheck.com", "+44 7700 900112", 1),
        TeamMemberEntity(8, "Carlos Medina", "Masonry Specialist", "c.medina@structures.co", "+44 7700 900223", 1)
    )

    fun getInitialInspections(): List<InspectionEntity> = listOf(
        InspectionEntity(
            id = 1,
            projectId = 1,
            title = "Granville Weekly HSE & Snagging Walkthrough",
            inspectorName = "John Smith",
            date = "12/01/2026",
            status = "In Progress",
            complianceScore = 78,
            siteArea = "Zone A: Exterior Logistics Apron & Entrances",
            weather = "Partly Cloudy (16°C), Dry Ground",
            notes = "Identified multiple waste and surface issues requiring urgent mitigation before next council review."
        ),
        InspectionEntity(
            id = 2,
            projectId = 1,
            title = "Fire Egress & Hazardous Material Survey",
            inspectorName = "Scott Cook",
            date = "05/01/2026",
            status = "Completed",
            complianceScore = 94,
            siteArea = "Block 1 to 4 Fire Corridors",
            weather = "Overcast, 12°C",
            notes = "All extinguishers tagged and illuminated signs verified. Minor paint touchup required."
        )
    )

    fun getInitialChecklistItems(): List<ChecklistItemEntity> = listOf(
        ChecklistItemEntity(1, 1, "Housekeeping & Waste", "Emergency exit routes 100% unobstructed", "Exit doors must swing freely and have clear 2-meter egress.", "PASS", "Verified clear"),
        ChecklistItemEntity(2, 1, "Housekeeping & Waste", "Waste collection areas and skip bins", "Check for overflowing commercial skips and loose debris.", "FAIL", "Large pile at Exit 2"),
        ChecklistItemEntity(3, 1, "Plumbing & Drainage", "Surface storm water gullies and drains", "Verify grates are free from silt, gravel, and organic debris.", "FAIL", "Drain by Garage 1 blocked"),
        ChecklistItemEntity(4, 1, "Structural & Safety", "Pavement, curbs, and concrete slabs", "Inspect for trip hazards, cracked concrete, and sinkage.", "FAIL", "Multiple damaged slabs"),
        ChecklistItemEntity(5, 1, "Housekeeping & Waste", "Hazardous storage and pallet stacking", "Hazardous canisters must have secondary containment and covers.", "FAIL", "Garage 4 pallets unsecured"),
        ChecklistItemEntity(6, 1, "Safety & Hazard", "Walkway marking and pedestrian paths", "Pedestrian routes must be cleanly demarcated and slip-resistant.", "PASS", "Paint spill cleaned"),
        ChecklistItemEntity(7, 1, "Structural & Safety", "Masonry and building perimeter integrity", "Check external columns, corner bumpers, and mortar lines.", "FLAG", "Chipped brick at North Entrance"),
        ChecklistItemEntity(8, 1, "Safety & Hazard", "Regulatory site signage and speed markers", "All safety signs, speed limits, and PPE advisories present.", "FAIL", "Missing road sign knocked down"),
        ChecklistItemEntity(9, 1, "Fire & PPE", "Fire extinguishers & manual call points", "Extinguishers tagged within 12 months with pin intact.", "PASS", "All tagged Dec 2025"),
        ChecklistItemEntity(10, 1, "Electrical", "Outdoor power distribution boards & cables", "All weatherproof enclosures sealed, cables protected from vehicle runover.", "PASS", "All sealed IP66")
    )
}
