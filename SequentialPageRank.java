/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SequentialPageRank
/*     */ {
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  26 */     System.out.println("*********************************************");
/*  27 */     System.out.println("* The part 1 of assignment#1 in B649 class  *");
/*  28 */     System.out.println("* sequential PageRank implemented with Java *");
/*  29 */     System.out.println("*********************************************");
/*     */ 
/*  31 */     if (paramArrayOfString.length != 4) {
/*  32 */       str1 = "\nUsage: java SequentialPageRank [input file path][output file path][iteration count][damping factor]\ne.g.: java SequentialPageRank pagerank.input pagerank.output 10 0.85\n";
/*     */ 
/*  37 */       System.out.println(str1);
/*  38 */       System.exit(-1);
/*     */     }
/*  40 */     String str1 = paramArrayOfString[0];
/*  41 */     String str2 = paramArrayOfString[1];
/*  42 */     int i = Integer.parseInt(paramArrayOfString[2]);
/*  43 */     double d = Double.parseDouble(paramArrayOfString[3]);
/*  44 */     if ((d < 0.0D) || (d > 1.0D)) {
/*  45 */       System.out.println("damping factor should between 0.0 and 1.0");
/*  46 */       System.exit(-1);
/*     */     }
/*     */ 
/*  51 */     long l1 = System.currentTimeMillis();
/*  52 */     run_pagerank(str1, str2, i, d);
/*  53 */     long l2 = System.currentTimeMillis();
/*  54 */     System.out.println("PageRank job is done.");
/*  55 */     System.out.println("Job turnaround time:" + l2 - l1 / 1000.0D + " sec.");
/*     */   }
/*     */ 
/*     */   public static void run_pagerank(String paramString1, String paramString2, int paramInt, double paramDouble)
/*     */   {
/*  63 */     int i = 0;
/*  64 */     HashMap localHashMap1 = new HashMap();
/*     */ 
/*  66 */     HashMap localHashMap2 = new HashMap();
/*     */ 
/*  69 */     build_adjacency_matrix(localHashMap1, paramString1);
/*  70 */     i = localHashMap1.keySet().size();
/*     */ 
/*  72 */     if (i > 100) {
/*  73 */       System.out.println("study version, do not support PageRank job whose urls is more than 100.");
/*  74 */       System.out.println("program exit.");
/*  75 */       System.exit(-1);
/*     */     }
/*     */ 
/*  78 */     init_rank_value_table(localHashMap2, i);
/*     */ 
/*  82 */     System.out.format("Computing process for %d iterations:", new Object[] { Integer.valueOf(paramInt) });
/*  83 */     System.out.println();
/*     */ 
/*  85 */     for (int j = 0; j < paramInt; j++) {
/*  86 */       System.out.println("iteration:" + j);
/*  87 */       join_rvt_am(localHashMap2, localHashMap1, i, paramDouble);
/*  88 */       System.out.println();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  97 */       FileWriter localFileWriter = new FileWriter(paramString2);
/*  98 */       BufferedWriter localBufferedWriter = new BufferedWriter(localFileWriter);
/*  99 */       Iterator localIterator = localHashMap2.keySet().iterator();
/*     */ 
/* 101 */       while (localIterator.hasNext()) {
/* 102 */         int k = ((Integer)localIterator.next()).intValue();
/* 103 */         localBufferedWriter.write(k + " " + localHashMap2.get(Integer.valueOf(k)) + "\n");
/*     */       }
/* 105 */       localBufferedWriter.close();
    
/*     */     } catch (IOException localIOException) {
/* 107 */       localIOException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void join_rvt_am(HashMap<Integer, Double> paramHashMap, HashMap<Integer, ArrayList<Integer>> paramHashMap1, int paramInt, double paramDouble)
/*     */   {
/* 118 */     Iterator localIterator = paramHashMap1.keySet().iterator();
/*     */ 
/* 121 */     HashMap localHashMap = new HashMap();
/* 122 */     for (int k = 0; k < paramInt; k++) {
/* 123 */       localHashMap.put(Integer.valueOf(k), Double.valueOf(0.0D));
/*     */     }
/* 125 */     double d2 = 0.0D;
/*     */ 
/* 127 */     while (localIterator.hasNext()) {
/* 128 */       int i = ((Integer)localIterator.next()).intValue();
/* 129 */       ArrayList localArrayList = (ArrayList)paramHashMap1.get(Integer.valueOf(i));
/*     */ 
/* 131 */       int m = localArrayList.size();
/*     */ 
/* 135 */       System.out.format("src_url:%d cur_val:[%.3f] tar_urls_list:[ ", new Object[] { Integer.valueOf(i), paramHashMap.get(Integer.valueOf(i)) });
/* 136 */       for (int n = 0; n < m; n++)
/* 137 */         System.out.print(((Integer)localArrayList.get(n)).intValue() + " ");
/* 138 */       System.out.println("]");
/*     */ 
/* 140 */       for (n = 0; n < m; n++) {
/* 141 */         int j = ((Integer)localArrayList.get(n)).intValue();
/* 142 */         double d1 = ((Double)localHashMap.get(Integer.valueOf(j))).doubleValue() + ((Double)paramHashMap.get(Integer.valueOf(i))).doubleValue() / m;
/* 143 */         System.out.format("  ->tar_url:%d cur_val:%.3f added_val:%.3f\n", new Object[] { Integer.valueOf(j), paramHashMap.get(Integer.valueOf(j)), Double.valueOf(((Double)paramHashMap.get(Integer.valueOf(i))).doubleValue() / m) });
/* 144 */         localHashMap.put(Integer.valueOf(j), Double.valueOf(d1));
/*     */       }
/*     */ 
/* 147 */       if (m == 0) {
/* 148 */         d2 += ((Double)paramHashMap.get(Integer.valueOf(i))).doubleValue();
/*     */       }
/*     */     }
/*     */ 
/* 152 */     double d3 = d2 / paramInt;
/* 153 */     for (int i1 = 0; i1 < paramInt; i1++) {
/* 154 */       paramHashMap.put(Integer.valueOf(i1), Double.valueOf(((Double)localHashMap.get(Integer.valueOf(i1))).doubleValue() + d3));
/*     */     }
/*     */ 
/* 159 */     double d4 = 1.0D / paramInt;
/* 160 */     for (int i2 = 0; i2 < paramInt; i2++)
/* 161 */       paramHashMap.put(Integer.valueOf(i2), Double.valueOf(paramDouble * ((Double)paramHashMap.get(Integer.valueOf(i2))).doubleValue() + (1.0D - paramDouble) * d4));
/*     */   }
/*     */ 
/*     */   public static void init_rank_value_table(HashMap<Integer, Double> paramHashMap, int paramInt)
/*     */   {
/* 169 */     double d = 1.0D / paramInt;
/* 170 */     for (int i = 0; i < paramInt; i++)
/* 171 */       paramHashMap.put(Integer.valueOf(i), Double.valueOf(d));
/*     */   }
/*     */ 
/*     */   public static void build_adjacency_matrix(HashMap<Integer, ArrayList<Integer>> paramHashMap, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 187 */       FileReader localFileReader = new FileReader(paramString);
/* 188 */       BufferedReader localBufferedReader = new BufferedReader(localFileReader);
/*     */       String str;
/* 189 */       while ((str = localBufferedReader.readLine()) != null) {
/* 190 */         String[] arrayOfString = str.split(" ");
/* 191 */         int i = Integer.parseInt(arrayOfString[0]);
/* 192 */         ArrayList localArrayList = new ArrayList();
/* 193 */         for (int k = 1; k < arrayOfString.length; k++) {
/* 194 */           int j = Integer.parseInt(arrayOfString[k]);
/* 195 */           localArrayList.add(Integer.valueOf(j));
/*     */         }
/* 197 */         paramHashMap.put(Integer.valueOf(i), localArrayList);
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 200 */       localIOException.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/jiangc11/Downloads/B534Project1Part1/SeqPageRank/
 * Qualified Name:     SequentialPageRank
 * JD-Core Version:    0.6.1
 */