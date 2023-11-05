public class Integration {

    private static final String FILE_PATH = "/Users/arthurandersen/Documents/CSE110/Lab4/lib/Ground_beef_pasta_tomatoes.mp3";

    public static void main(String[] args) {
        try {
            // Use Whisper.java to transcribe audio to text
            String transcribedText = Whisper.transcribeAudio(FILE_PATH);

            // Use the transcribed text as a prompt for ChatGPT.java
            String chatGPTResponse = ChatGPT.getGPTResponse(transcribedText);

            // Output the response
            System.out.println("ChatGPT Response: " + chatGPTResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
