package cq_server.model;

public class BaseResponse {
	private final BaseResponseType type;

	public BaseResponse(final BaseResponseType type) {
		this.type = type;
	}

	public BaseResponseType getType() {
		return this.type;
	}
}
