# Wolf-Parking-Database-Management


The primary project narrative for the Database Management Concepts and Systems course revolves around developing the Wolf Parking Management System. It aims to model the Wolf Parking Management System based on insights obtained from the university parking manager and designed to oversee campus parking lots and users. This project handles parking lot availability, zone specifications, permit assignments, and citation tracking. Administrators manage space availability, assign permits, track citations, and generate reports for efficient parking management.

It's intended to maintain comprehensive information that covers all the below following key aspects:

Driver Information: Including names, statuses denoted as ‘S’ (Student), ‘E’ (Employee), or ‘V’ (Visitor), and respective IDs or phone numbers as unique identifiers.

Parking Lot Information: Details encompassing lot names, addresses, zones, and spaces within the lots.

Zone Information: Defined by IDs (A, B, C, D, AS, BS, CS, DS, V) indicating various categories, primarily distinguishing zones for employees, students, and visitors.

Space Information: Involving space numbers, types (e.g., "electric," "handicap," "compact car," defaulting to "regular"), and availability status.

Permit Information: Covering permit IDs, lot details, zone IDs, space types, car license numbers, validity period, associated driver IDs or phone numbers, and permit types (such as "residential," "commuter," "peak hours," "special event," and "Park & Ride").

Vehicle Information: Encompassing license plate numbers, vehicle models, colors, manufacturers, and manufacturing years.

Citation Information: Tracking citation numbers, car details, citation dates, times, lots, categories (e.g., "Invalid Permit," "Expired Permit," "No Permit"), fees, and payment statuses.

. However, not all information provided may need explicit capture in the database. Hence, part of the project's effort involves discerning what to retain or discard, making assumptions, and addressing potential inconsistencies while documenting these decisions and assumptions.

The system administrators will have capabilities to manage parking lots, zones, spaces, permits, and citations. They can assign permits, manage vehicle lists, alter space availabilities, and verify permit validity within their respective lots. Compliance requires permits aligned with designated lot zones and types of parking spaces.

Additionally, security personnel can create/update/delete citations for parking violations, modifying payment statuses as required. Database records will encompass driver, parking lot, permit, and citation data.

Students and visitors are limited to one vehicle per permit, while employees can have up to two. Specific allowances are made for special events or Park & Ride scenarios. Tasks involving information processing, permit management, citation handling, and report generation will drive system functionalities.

These tasks encompass diverse operations, such as managing driver and lot information, assigning permits, tracking citations, and generating reports.


The 3 project reports have information about the database schema and it's design

Report 1: ER Diagrams and APIs
Report 2: Database Schema and SQL statements that create the tables
Report 3: Transactions used in the Database and how the code was structured
