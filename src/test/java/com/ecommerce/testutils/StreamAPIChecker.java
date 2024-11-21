package com.ecommerce.testutils;

import java.io.IOException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.ecommerce.inventory.InventoryService;

public class StreamAPIChecker extends ClassVisitor {
	private final Method method;
	private boolean streamApiFound;

	public StreamAPIChecker(Method method) {
		super(Opcodes.ASM9);
		this.method = method;
		this.streamApiFound = false;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		if (name.equals(method.getName()) && descriptor.equals(getMethodDescriptor(method))) {
			return new StreamApiFinderMethodVisitor();
		}
		return null; // Skip other methods
	}

	public boolean isStreamApiFound() {
		return streamApiFound;
	}

	private class StreamApiFinderMethodVisitor extends MethodVisitor {
		public StreamApiFinderMethodVisitor() {
			super(Opcodes.ASM9);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
			// Check if the method is part of the Stream API
			if (owner.startsWith("java/util/stream")) {
				streamApiFound = true; // Stream API detected
			}
			super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
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

	public static boolean testStreamApiUsage(String methodName, Class<?>... parameterTypes) throws IOException {
		try {
			// Retrieve the method dynamically using the provided parameter types
			Method targetMethod = InventoryService.class.getDeclaredMethod(methodName, parameterTypes);

			// Use ClassReader to analyze bytecode
			String className = InventoryService.class.getName().replace('.', '/');
			ClassReader classReader = new ClassReader(className);

			// Create and use the StreamApiFinderClassVisitor
			StreamAPIChecker visitor = new StreamAPIChecker(targetMethod);
			classReader.accept(visitor, 0);

			// Check if Stream API is used
			boolean usesStreamApi = visitor.isStreamApiFound();
			System.out.println("The " + methodName + " method uses Stream API: " + usesStreamApi);

			return usesStreamApi;
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
	}
}
