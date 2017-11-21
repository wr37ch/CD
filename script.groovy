public static void main(String[] args) {
    while (true){
        def text
        println 'What would you like to do? Write PULL/PUSH or exit'
        def action = new Scanner(System.in).nextLine()
        if (action == "PULL") {
            println 'Type the name of the repo'
            def choice = new Scanner(System.in).nextLine()

            def get = new URL($/http://50.50.50.50:8081/service/siesta/rest/beta/components?repositoryId=${choice}/$).openConnection()
            def getRC = get.getResponseCode()
            println(getRC)
            if (getRC.equals(200)) {
                text = get.getInputStream().getText()

            }
            else if (!getRC.equals(200)){
                println("Can't connect to this repo, check repo name")
                continue
            }
            def slurper = new groovy.json.JsonSlurper()
            def result = slurper.parseText(text)

            for (int i = 0; i < result.items.size(); i++) {

                println("GAV parameters: ")
                print "(Name — "+result.items[i].name + " | "
                print "Group — " +result.items[i].group + " | "
                print "Version — " +result.items[i].version + " | "
                println "id — "+result.items[i].id+ " )"

            }
            println "Write down which artifact you'd like to download"
            def artifact = new Scanner(System.in).nextLine()
            for (int i = 0; i < result.items.size(); i++) {
                if (result.items[i].name == artifact ){
                    def a = result.items[i].assets[0].downloadUrl
                    new File("/opt/trash/${artifact}"+"-"+result.items[i].version+".zip").withOutputStream { out ->
                        new URL(a).eachByte { b ->
                            out.write(b)
                        }
                    }
                    println("Succesfully downloaded")
                }
                
            }


        }
        else if(action == "PUSH"){

        }
        else if(action == "exit"){
            break
        }
        else {
            println "Choose the right option"
            continue
        }
    }}
