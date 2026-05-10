package transactions_dispute_portal.backend.dto;

public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String cellphone;

    public UserResponseDTO() {
    }

    public UserResponseDTO(
            Long id,
            String firstName,
            String lastName,
            String username,
            String cellphone
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.cellphone = cellphone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}