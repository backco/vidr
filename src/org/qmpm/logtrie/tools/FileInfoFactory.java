/*
 * 	LogTrie - an efficient data structure and CLI for XES event logs and other sequential data
 * 
 * 	Author: Christoffer Olling Back	<www.christofferback.com>
 * 
 * 	Copyright (C) 2018 University of Copenhagen 
 * 
 *	This file is part of LogTrie.
 *
 *	LogTrie is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	LogTrie is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with LogTrie.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.qmpm.logtrie.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileInfoFactory {
	
	@SuppressWarnings("unchecked")
	public static <T extends Collection<? extends List<? extends Object>>> FileInfo<T> build(String path) {
		
		if (path.toLowerCase().endsWith("xes")) {
			return (FileInfo<T>) new XLogFile(path);
		}
		
		return null;
	}

	public static <T extends Collection<? extends List<? extends Object>>> FileInfo<T> combine(List<FileInfo<T>> fiList) {
		
		System.out.println("In FileInfoFactory.combine()");
		System.out.println("fiList.size(): " + fiList.size());
		
		for (FileInfo fi : fiList) {
			System.out.println("size: " + fi.getLoadedFile().size());
		}
		
		FileInfo<T> result = fiList.get(0).shallowCopy();
		
		for (FileInfo<T> fi : fiList) {
			System.out.println("result.size(): " + result.getLoadedFile().size() + " appending FileInfo of size " + fi.getLoadedFile().size());
			result.append(fi);
		}
		System.out.println("result.size(): " + result.getLoadedFile().size());
		return result;
	}
	
	public static <T extends Collection<? extends List<? extends Object>>> List<FileInfo<T>> partition(FileInfo<T> fi, int k) {
		
		List<FileInfo<T>> result = new ArrayList<FileInfo<T>>();
		
		System.out.println("In FileInfoFactory.partition(): fi.getLoadedFile().size(): " + fi.getLoadedFile().size());
		
		int i = Math.floorDiv(fi.getLoadedFile().size(), k);
		int r = fi.getLoadedFile().size() % k;
		int shift = 0;
		for (int j=0; j<k; j++) {
			
			FileInfo<T> filePart = fi.clone();
						
			System.out.println("In FileInfoFactory.partition(): trying to make partition from indices: " + (j * i + shift) + " to " + (j * i + i + (r > 0 ? 1 : 0) + shift));		
			filePart.cutDownFile(j * i + shift, (j * i + i + (r > 0 ? 1 : 0) + shift));
			System.out.println("filePart.size(): " + filePart.getLoadedFile().size());
			shift += r > 0 ? 1 : 0;
			r--;
			result.add(filePart);
		}

		return result;
	}
}
