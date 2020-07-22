package io.spotnext.seal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

public interface Sealable<T> {
	static final ByteBuddy BYTE_BUDDY = new ByteBuddy();
//	static Map<Class<?>, Class<?>> SEALED_TYPES = new HashMap<>();

	default T seal() {
		final ThrowsExceptionInterceptor setterInterceptor = new ThrowsExceptionInterceptor();

		try {
//			var sealedType = SEALED_TYPES.get(this.getClass());
			var sealedType = BYTE_BUDDY
					.subclass(this.getClass())
					.method(ElementMatchers.any())
					.intercept(MethodDelegation.to(this))
					.method(ElementMatchers.isSetter())
					.intercept(MethodDelegation.to(setterInterceptor))
					.make()
					.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
					.getLoaded();

//				SEALED_TYPES.put(this.getClass(), sealedType);

			final T sealedObject = (T) sealedType.newInstance();

			return sealedObject;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static class ThrowsExceptionInterceptor implements InvocationHandler {
		@Override
		@RuntimeType
		public Object invoke(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Throwable {
			throw new UnsupportedOperationException("This instance is sealed");
		}
	}

}
