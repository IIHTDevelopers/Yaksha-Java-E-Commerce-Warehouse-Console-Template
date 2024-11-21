package com.ecommerce.testutils;

import java.io.IOException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.ecommerce.inventory.InventoryService;

public class LambdaChecker extends ClassVisitor {
	private final Method method;
	private boolean lambdaFound;

	public LambdaChecker(Method method) {
		super(Opcodes.ASM9);
		this.method = method;
		this.lambdaFound = false;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		if (name.equals(method.getName()) && descriptor.equals(getMethodDescriptor(method))) {
			return new LambdaFinderMethodVisitor();
		}
		return null;
	}

	public boolean isLambdaFound() {
		return lambdaFound;
	}

	private class LambdaFinderMethodVisitor extends MethodVisitor {
		public LambdaFinderMethodVisitor() {
			super(Opcodes.ASM9);
		}

		@Override
		public void visitInvokeDynamicInsn(String name, String descriptor, Handle bsm, Object... bsmArgs) {
			if ("java/lang/invoke/LambdaMetafactory".equals(bsm.getOwner())) {
				lambdaFound = true;
			}
		}
	}

	private String getMethodDescriptor(Method method) {
		// Get the descriptor for the method's return type
		String returnTypeDescriptor = Type.getDescriptor(method.getReturnType());

		// Get the descriptors for the method's parameter types
		Class<?>[] parameterTypes = method.getParameterTypes();
		StringBuilder parameterDescriptors = new StringBuilder();
		for (Class<?> parameterType : parameterTypes) {
			parameterDescriptors.append(Type.getDescriptor(parameterType));
		}

		// Construct the method descriptor
		return "(" + parameterDescriptors + ")" + returnTypeDescriptor;
	}

	public static boolean testLambdaUsage(String methodName, Class<?>... parameterTypes) throws IOException {
		try {
			Method addProductMethod = InventoryService.class.getDeclaredMethod(methodName, parameterTypes);
			String className = InventoryService.class.getName().replace('.', '/');
			ClassReader classReader = new ClassReader(className);
			LambdaChecker visitor = new LambdaChecker(addProductMethod);
			classReader.accept(visitor, 0);
			boolean usesLambda = visitor.isLambdaFound();
			System.out.println("The " + methodName + " method uses lambda: " + usesLambda);

			return usesLambda;
		} catch (Exception ex) {
			return false;
		}
	}
}
