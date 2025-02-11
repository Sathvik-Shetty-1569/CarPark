package apcoders.in.carpark.models.models;

public class GoogleDriveFile {
    private String id;
    private String name;
    private String webViewLink;

    public GoogleDriveFile(String id, String name, String webViewLink) {
        this.id = id;
        this.name = name;
        this.webViewLink = webViewLink;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getWebViewLink() { return webViewLink; }
}
