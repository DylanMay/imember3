package com.yundong.imember.http;

public interface GetServiceDataHandlerListener {

	void process_retry_handler();
	void process_cancel_handler();
	void process_success_handler(String response);
	int process_fail_handler(String response);
}
