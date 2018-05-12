/**
 * Copyright 2015-2025 FLY的狐狸(email:jflyfox@sina.com qq:369191470).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.jcore.jfinal.interceptor;

import com.jcore.web.vo.RtnFactory;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.JFinalKit;
import com.jfinal.log.Log;

public class ExceptionInterceptor implements Interceptor {

	private final static Log log = Log.getLog(ExceptionInterceptor.class);

	public void intercept(Invocation inv) {

		try {
			inv.invoke();
		} catch (Exception e) {
			log.error("EXCEPTION:{}", e);

			Controller controller = inv.getController();
			String methodName = inv.getMethodName();
			if (methodName.substring(0, 2).equals("go")) {
				controller.render(JFinalKit.getConstants().getErrorView(500));
			}else {
				controller.renderJson(RtnFactory.exception.toJsonString());
			}
		}

	}
}
