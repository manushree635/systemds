/**
 * IBM Confidential
 * OCO Source Materials
 * (C) Copyright IBM Corp. 2010, 2015
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what has been deposited with the U.S. Copyright Office.
 */

package com.ibm.bi.dml.runtime.controlprogram.context;

//import java.util.LinkedList;
//import java.util.List;
//
//import org.apache.hadoop.mapred.SequenceFileInputFormat;
//
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.broadcast.Broadcast;
//
//import scala.Tuple2;
//
import com.ibm.bi.dml.runtime.DMLRuntimeException;
//import com.ibm.bi.dml.runtime.DMLUnsupportedOperationException;
import com.ibm.bi.dml.runtime.controlprogram.Program;
//import com.ibm.bi.dml.runtime.controlprogram.caching.MatrixObject;
import com.ibm.bi.dml.runtime.matrix.data.MatrixBlock;
//import com.ibm.bi.dml.runtime.matrix.data.MatrixIndexes;


public class SparkExecutionContext extends ExecutionContext
{
	@SuppressWarnings("unused")
	private static final String _COPYRIGHT = "Licensed Materials - Property of IBM\n(C) Copyright IBM Corp. 2010, 2015\n" +
                                             "US Government Users Restricted Rights - Use, duplication  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.";

//	private JavaSparkContext _spctx = null; 
//	
	protected SparkExecutionContext(Program prog) 
	{
		//protected constructor to force use of ExecutionContextFactory
		this( true, prog );
	}

	protected SparkExecutionContext(boolean allocateVars, Program prog) 
	{
		//protected constructor to force use of ExecutionContextFactory
		super( allocateVars, prog );

//		//create a default spark context (master, appname, etc refer to system properties
//		//as given in the spark configuration or during spark-submit)
//		_spctx = new JavaSparkContext();
	}
//	
//	public JavaSparkContext getSparkContext()
//	{
//		return _spctx;
//	}
//	
//	/**
//	 * This call returns an RDD handle for a given variable name. This includes 
//	 * the creation of RDDs for in-memory or binary-block HDFS data. 
//	 * 
//	 * Spark instructions should call this for all matrix inputs except broadcast
//	 * variables. 
//	 * 
//	 * TODO for reblock we should directly rely on the filename 
//	 * 
//	 * @param varname
//	 * @return
//	 * @throws DMLRuntimeException 
//	 * @throws DMLUnsupportedOperationException 
//	 */
//	@SuppressWarnings("unchecked")
//	public JavaPairRDD<MatrixIndexes,MatrixBlock> getRDDHandleForVariable( String varname ) 
//		throws DMLRuntimeException, DMLUnsupportedOperationException
//	{
//		MatrixObject mo = getMatrixObject(varname);
//		
//		//NOTE: MB this logic should be integrated into MatrixObject
//		//However, for now we cannot assume that spark libraries are 
//		//always available and hence only store generic references in 
//		//matrix object while all the logic is in the SparkExecContext
//		
//		JavaPairRDD<MatrixIndexes,MatrixBlock> rdd = null;
//		//CASE 1: rdd already existing 
//		if( mo.getRDDHandle()!=null )
//		{
//			rdd = (JavaPairRDD<MatrixIndexes, MatrixBlock>) mo.getRDDHandle();
//		}
//		//CASE 2: dirty in memory data
//		else if( mo.isDirty() )
//		{
//			//get in-memory matrix block and parallelize it
//			MatrixBlock mb = mo.acquireRead(); //pin matrix in memory
//			rdd = toJavaPairRDD(_spctx, mb, (int)mo.getNumRowsPerBlock(), (int)mo.getNumColumnsPerBlock());
//			mo.release(); //unpin matrix
//			
//			//keep rdd handle for future operations on it
//			mo.setRDDHandle(rdd);
//		}
//		//CASE 3: non-dirty (file exists on HDFS)
//		else
//		{
//			//parallelize hdfs-resident file
//			rdd = _spctx.hadoopFile( mo.getFileName(), SequenceFileInputFormat.class, MatrixIndexes.class, MatrixBlock.class);
//			
//			//keep rdd handle for future operations on it
//			mo.setRDDHandle(rdd);
//		}
//		
//		return rdd;
//	}
//	
//	/**
//	 * 
//	 * @param varname
//	 * @return
//	 * @throws DMLRuntimeException
//	 * @throws DMLUnsupportedOperationException
//	 */
//	public Broadcast<MatrixBlock> getBroadcastForVariable( String varname ) 
//		throws DMLRuntimeException, DMLUnsupportedOperationException
//	{
//		MatrixObject mo = getMatrixObject(varname);
//		
//		//read data into memory (no matter where it comes from)
//		MatrixBlock mb = mo.acquireRead();
//		Broadcast<MatrixBlock> bret = _spctx.broadcast(mb);
//		mo.release();
//		
//		return bret;
//	}
//	
//	/**
//	 * Keep the output rdd of spark rdd operations as meta data of matrix objects in the 
//	 * symbol table.
//	 * 
//	 * Spark instructions should call this for all matrix outputs.
//	 * 
//	 * 
//	 * @param varname
//	 * @param rdd
//	 * @throws DMLRuntimeException 
//	 */
//	public void setRDDHandleForVariable(String varname, JavaPairRDD<MatrixIndexes,MatrixBlock> rdd) 
//		throws DMLRuntimeException
//	{
//		MatrixObject mo = getMatrixObject(varname);
//		
//		mo.setRDDHandle( rdd );
//	}
//	
//	/**
//	 * Utility method for creating an RDD out of an in-memory matrix block.
//	 * 
//	 * @param sc
//	 * @param block
//	 * @return
//	 * @throws DMLUnsupportedOperationException 
//	 * @throws DMLRuntimeException 
//	 */
//	public static JavaPairRDD<MatrixIndexes,MatrixBlock> toJavaPairRDD(JavaSparkContext sc, MatrixBlock src, int brlen, int bclen) 
//		throws DMLRuntimeException, DMLUnsupportedOperationException
//	{
//		LinkedList<Tuple2<MatrixIndexes,MatrixBlock>> list = new LinkedList<Tuple2<MatrixIndexes,MatrixBlock>>();
//		
//		if(    src.getNumRows() <= brlen 
//		    && src.getNumColumns() <= bclen )
//		{
//			list.addLast(new Tuple2<MatrixIndexes,MatrixBlock>(new MatrixIndexes(1,1), src));
//		}
//		else
//		{
//			boolean sparse = src.isInSparseFormat();
//			
//			//create and write subblocks of matrix
//			for(int blockRow = 0; blockRow < (int)Math.ceil(src.getNumRows()/(double)brlen); blockRow++)
//				for(int blockCol = 0; blockCol < (int)Math.ceil(src.getNumColumns()/(double)bclen); blockCol++)
//				{
//					int maxRow = (blockRow*brlen + brlen < src.getNumRows()) ? brlen : src.getNumRows() - blockRow*brlen;
//					int maxCol = (blockCol*bclen + bclen < src.getNumColumns()) ? bclen : src.getNumColumns() - blockCol*bclen;
//					
//					MatrixBlock block = new MatrixBlock(maxRow, maxCol, sparse);
//						
//					int row_offset = blockRow*brlen;
//					int col_offset = blockCol*bclen;
//	
//					//copy submatrix to block
//					src.sliceOperations( row_offset+1, row_offset+maxRow, 
//							             col_offset+1, col_offset+maxCol, 
//							             block );							
//					
//					//append block to sequence file
//					MatrixIndexes indexes = new MatrixIndexes(blockRow+1, blockCol+1);
//					list.addLast(new Tuple2<MatrixIndexes,MatrixBlock>(indexes, block));
//				}
//		}
//		
//		return sc.parallelizePairs(list);
//	}
	
	/**
	 * This method is a generic abstraction for calls from the buffer pool.
	 * See toMatrixBlock(JavaPairRDD<MatrixIndexes,MatrixBlock> rdd, int numRows, int numCols);
	 * 
	 * @param rdd
	 * @param numRows
	 * @param numCols
	 * @return
	 * @throws DMLRuntimeException 
	 */
//	@SuppressWarnings("unchecked")
	public static MatrixBlock toMatrixBlock(Object rdd, int rlen, int clen, int brlen, int bclen) 
		throws DMLRuntimeException
	{
//		JavaPairRDD<MatrixIndexes,MatrixBlock> lrdd = (JavaPairRDD<MatrixIndexes,MatrixBlock>) rdd;
//		return toMatrixBlock(lrdd, rlen, clen, brlen, bclen);
		return null;
	}
	
//	/**
//	 * Utility method for creating a single matrix block out of an RDD. Note that this collect call
//	 * might trigger execution of any pending transformation. 
//	 * 
//	 * TODO add a more efficient path for sparse matrices, see BinaryBlockReader.
//	 * 
//	 * @param rdd
//	 * @param numRows
//	 * @param numCols
//	 * @return
//	 * @throws DMLRuntimeException
//	 */
//	public static MatrixBlock toMatrixBlock(JavaPairRDD<MatrixIndexes,MatrixBlock> rdd, int rlen, int clen, int brlen, int bclen) 
//		throws DMLRuntimeException
//	{
//		//current assumption always dense
//		MatrixBlock out = new MatrixBlock(rlen, clen, false);
//		List<Tuple2<MatrixIndexes,MatrixBlock>> list = rdd.collect();
//		
//		for( Tuple2<MatrixIndexes,MatrixBlock> keyval : list )
//		{
//			MatrixIndexes ix = keyval._1();
//			MatrixBlock block = keyval._2();
//			
//			int row_offset = (int)(ix.getRowIndex()-1)*brlen;
//			int col_offset = (int)(ix.getColumnIndex()-1)*bclen;
//			int rows = block.getNumRows();
//			int cols = block.getNumColumns();
//			
//			out.copy( row_offset, row_offset+rows-1, 
//					  col_offset, col_offset+cols-1,
//					  block, false );			
//		}
//		
//		out.recomputeNonZeros();
//		out.examSparsity();
//		
//		return out;
//	}
}
