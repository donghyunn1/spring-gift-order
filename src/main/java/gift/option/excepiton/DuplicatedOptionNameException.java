package gift.option.excepiton;

public class DuplicatedOptionNameException extends RuntimeException {
  public DuplicatedOptionNameException(String message) {
    super(message);
  }
}
