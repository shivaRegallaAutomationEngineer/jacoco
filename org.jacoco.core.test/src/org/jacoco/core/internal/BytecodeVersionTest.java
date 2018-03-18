/*******************************************************************************
 * Copyright (c) 2009, 2018 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.core.internal;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class BytecodeVersionTest {

	@Test
	public void should_return_original_when_not_java10() {
		final byte[] originalBytes = createClass(Opcodes.V9);

		final byte[] bytes = BytecodeVersion.downgradeIfNeeded(Opcodes.V9,
				originalBytes);

		assertSame(originalBytes, bytes);
	}

	@Test
	public void should_return_copy_when_java10() {
		final byte[] originalBytes = createClass(BytecodeVersion.V10);

		final byte[] bytes = BytecodeVersion
				.downgradeIfNeeded(BytecodeVersion.V10, originalBytes);

		assertNotSame(originalBytes, bytes);
		assertEquals(Opcodes.V9, BytecodeVersion.read(bytes));
		assertEquals(BytecodeVersion.V10, BytecodeVersion.read(originalBytes));
	}

	private static byte[] createClass(final int version) {
		final ClassWriter cw = new ClassWriter(0);
		cw.visit(version, 0, "Foo", null, "java/lang/Object", null);
		cw.visitEnd();
		return cw.toByteArray();
	}

}
