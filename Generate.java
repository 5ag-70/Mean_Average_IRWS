import java.util.Scanner; 
import java.util.Random; 
import java.io.PrintWriter;
import java.io.FileWriter; 
import java.io.File; 

class Generate {
	public static void main(String[] args) throws Exception {
		
        // Using Scanner for Getting Input from User 
        Scanner input = new Scanner(System.in); 
		
		System.out.println("Enter file name you want to generate");
        String file_name = input.nextLine(); 
        System.out.println("File name would be "+file_name); 
		
  
		System.out.println("How many engine you want to generate(1-26)");
        int engines = input.nextInt(); 
        System.out.println("You want engines "+engines); 

		System.out.println("How many runs per engine (1-3)");
        int runs = input.nextInt(); 
        System.out.println("You want " + runs + " runs per engine"); 
		
		Random random = new Random();
		String run_letters = "RUN";
		String[] engine_names = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		File file = new File(file_name);
		PrintWriter output_file = (new PrintWriter(new FileWriter(file)));
		
		for(int i=1; i<=runs; i++) {
			for(int j=0; j<engines; j++) {
				String query = "";
				int r_in_query = 0;
				for(int k=0; k<21; k++) {
					char letter = run_letters.charAt(random.nextInt(run_letters.length()));
					if(letter == 'R')
						r_in_query++;
					query = query + letter;
				}
				int related_documents = random.nextInt(5)+r_in_query;
				String output_words = i + ";" + engine_names[j] + ";" + query + ";" +related_documents;
				System.out.println(output_words);
				output_file.write(output_words);
				output_file.write(System.getProperty("line.separator"));
			}
		}
		output_file.close();
	}
}