package org.testing.project.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class TAR {

	public static void gzipFile(String source_filepath, String destinaton_zip_filepath) {

		byte[] buffer = new byte[1024];

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destinaton_zip_filepath);
			GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);
			FileInputStream fileInput = new FileInputStream(source_filepath);
			int bytes_read;
			while ((bytes_read = fileInput.read(buffer)) > 0) {
				gzipOuputStream.write(buffer, 0, bytes_read);
			}
			fileInput.close();
			gzipOuputStream.finish();
			gzipOuputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void compress(String name, File... files) throws IOException {
		try (TarArchiveOutputStream out = getTarArchiveOutputStream(name)) {
			for (File file : files) {
				addToArchiveCompression(out, file, ".");
			}
		}
	}

	public static void decompress(String in, File out) throws IOException {
		try (TarArchiveInputStream fin = new TarArchiveInputStream(new FileInputStream(in))) {
			TarArchiveEntry entry;
			while ((entry = fin.getNextTarEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}
				File curfile = new File(out, entry.getName());
				File parent = curfile.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				IOUtils.copy(fin, new FileOutputStream(curfile));
			}
		}
	}

	private static TarArchiveOutputStream getTarArchiveOutputStream(String name) throws IOException {
		TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(name));
		taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
		taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		taos.setAddPaxHeadersForNonAsciiNames(true);
		return taos;
	}

	private static void addToArchiveCompression(TarArchiveOutputStream out, File file, String dir) throws IOException {
		String entry = dir + File.separator + file.getName();
		if (file.isFile()) {
			out.putArchiveEntry(new TarArchiveEntry(file, entry));
			try (FileInputStream in = new FileInputStream(file)) {
				IOUtils.copy(in, out);
			}
			out.closeArchiveEntry();
		} else if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (File child : children) {
					addToArchiveCompression(out, child, entry);
				}
			}
		} else {
			System.out.println(file.getName() + " is not supported");
		}
	}
}
