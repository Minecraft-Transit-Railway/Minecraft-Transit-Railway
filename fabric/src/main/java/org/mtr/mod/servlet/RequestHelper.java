package org.mtr.mod.servlet;


import org.mtr.core.Main;
import org.mtr.libraries.okhttp3.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public final class RequestHelper {

	private final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).writeTimeout(2, TimeUnit.SECONDS).readTimeout(2, TimeUnit.SECONDS).build();

	public void sendRequest(String url, @Nullable String content, @Nullable BiConsumer<String, String> callback) {
		final Request.Builder requestBuilder = new Request.Builder().url(url);
		final Request request;
		if (content == null) {
			request = requestBuilder.get().build();
		} else {
			request = requestBuilder.post(RequestBody.create(content, MediaType.get("application/json"))).build();
		}

		final Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				if (!(e instanceof InterruptedIOException)) {
					Main.LOGGER.error(call.request().url(), e);
				}
			}

			@Override
			public void onResponse(Call call, Response response) {
				try (final ResponseBody responseBody = response.body()) {
					if (callback != null) {
						callback.accept(responseBody.string(), response.request().url().url().getFile());
					}
				} catch (IOException e) {
					if (!(e instanceof InterruptedIOException)) {
						Main.LOGGER.error(call.request().url(), e);
					}
				}
			}
		});
	}
}
