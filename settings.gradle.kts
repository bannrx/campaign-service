rootProject.name = "campaignService"

//Uncomment below section only for local testing
include("bannrx-common")
project(":bannrx-common").projectDir = File("../bannrx-common")
include("utility")
project(":utility").projectDir = File("../utility")