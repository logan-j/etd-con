<?xml version='1.0' encoding="UTF-8" ?>

<!-- Copyright 2012 The University of Iowa
	Originally created by Shawn Averkamp and Joanna Lee
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>. -->
	
<!--
	1/15/10 - removed title case option from names (author and advisor) to retain capilization as input since most were not in all caps; 
	added bibliographic page number section; added note for optimized pdf; standardized degree names, added document-type and disciplines, replaced encoding in abstract,
	changed title to only change case if no lower case vowels
	4/29/10 - changed encoding to UTF-8; added language
	7/7/10 moved local fields to degree name, department, language; added abstract_format
-->

<!-- 
	2013 - XSL was acquired and heavily modified by Logan Jewett for usage by Iowa State University. Many areas were
	added, removed, or modified.
 -->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:str="http://www.metaphoricalweb.org/xmlns/string-utilities"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:util="http://blank">
	
	<xsl:output method="xml" indent="yes" />

	<!-- Transform of ProQuest XML to Digital Commons XML Schema for Electronic Theses and Dissertations (ETDs) -->

	<!-- function to transform title from all caps to title case (stopwords included) -->
	<xsl:function name="str:title-case" as="xs:string">
		<xsl:param name="expr"/>
		<xsl:variable name="tokens" select="tokenize($expr, '(~)|( )')"/>
		<xsl:variable name="titledTokens"
			select="for $token in $tokens return 
			concat(upper-case(substring($token,1,1)),
			lower-case(substring($token,2)))"/>
		<xsl:value-of select="$titledTokens"/>
	</xsl:function>
	
	<xsl:function name="util:strip-tags">
  		<xsl:param name="text"/>
  			<xsl:choose>
   				<xsl:when test="contains($text, '&lt;')">
     				 <xsl:value-of select="concat(substring-before($text, '&lt;'),
       					 util:strip-tags(substring-after($text, '&gt;')))"/>
    			</xsl:when>
    			<xsl:otherwise>
     	 			<xsl:value-of select="$text"/>
    			</xsl:otherwise>
  			</xsl:choose>
	</xsl:function>

	<xsl:template match="/">
		<documents xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:noNamespaceSchemaLocation="http://www.bepress.com/document-import.xsd">

			<xsl:for-each select="DISS_submission">
				<document>

					<title>
						<xsl:variable name="title" select="DISS_description/DISS_title"/>

						<xsl:choose>
							<xsl:when test="contains($title,'a')">
								<xsl:value-of select="normalize-space($title)"/>
							</xsl:when>
							<xsl:when test="contains($title,'e')">
								<xsl:value-of select="normalize-space($title)"/>
							</xsl:when>
							<xsl:when test="contains($title,'i')">
								<xsl:value-of select="normalize-space($title)"/>
							</xsl:when>
							<xsl:when test="contains($title,'o')">
								<xsl:value-of select="normalize-space($title)"/>
							</xsl:when>
							<xsl:when test="contains($title,'u')">
								<xsl:value-of select="normalize-space($title)"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="title" select="DISS_description/DISS_title"/>
								<xsl:variable name="hyphentitle" select="replace($title, '-', '-~')"/>
								<xsl:value-of
									select="normalize-space(replace(str:title-case((util:strip-tags($hyphentitle))), '- ', '-'))"
								/>
							</xsl:otherwise>
						</xsl:choose>
					</title>

					<!-- We've used DISS_comp_date as our publication date, which is generally represented as yyyy, but if DISS_accept_date is preferred, this will transform date to ISO 8601 format (yyyy-mm-dd).  -->
					<publication-date>
						<xsl:variable name="datestr">
							<xsl:value-of select="DISS_description/DISS_dates/DISS_comp_date"/>
						</xsl:variable>
						<xsl:value-of select="concat($datestr, '-01-01')"/>
					</publication-date>
					<publication_date_date_format>YYYY-MM-DD</publication_date_date_format>
					
					<!-- Author -->
					<authors>
						<xsl:for-each select="DISS_authorship/DISS_author">
							<author xsi:type="individual">
								<email>
										<xsl:value-of select="DISS_contact[@type='current']/DISS_email"/>
								</email>
								<institution>Iowa State University</institution>
								<lname>
									<xsl:value-of select="DISS_name/DISS_surname"/>
								</lname>
								<fname>
									<xsl:value-of select="DISS_name/DISS_fname"/>
								</fname>
								<mname>
									<xsl:value-of select="DISS_name/DISS_middle"/>
								</mname>
								<suffix>
									<xsl:value-of select="DISS_name/DISS_suffix"/>
								</suffix>
								
								
							</author>
						</xsl:for-each>
					</authors>

					<!-- changed the organization involving changing the discipline -->

					<disciplines>
						<xsl:for-each select="DISS_description/DISS_categorization/DISS_category/DISS_cat_desc">
							<discipline>
								<xsl:variable name="discipline">
									<xsl:value-of select="."/>
								</xsl:variable>
								<xsl:value-of select="$discipline"/>
							</discipline>
						</xsl:for-each>
					</disciplines>

					<!-- Outputs each keyword into its own keyword element , splitting on both semicolon and comma-->
					<keywords>
						<xsl:for-each select="DISS_description/DISS_categorization/DISS_keyword">
							<xsl:variable name="keywordstring">
								<xsl:value-of select="translate(., ';', ',')"/>
							</xsl:variable>
							<xsl:variable name="tokenkeyword"
								select="tokenize($keywordstring, ',\s+')"/>
							<xsl:for-each select="$tokenkeyword">
								<keyword>
									<xsl:value-of select="."/>
								</keyword>
							</xsl:for-each>
						</xsl:for-each>
					</keywords>

					<!-- Abstract  - replaces ProQuest formatting characters to bepress formatting -->
					<abstract>
						<xsl:for-each select="DISS_content/DISS_abstract">
							<xsl:for-each select="DISS_para">
								<xsl:variable name="abstract">
									<xsl:value-of select="."/>
								</xsl:variable>
								<xsl:if test="$abstract!='Abstract'">
									<p>
										<xsl:value-of
											select="concat(normalize-space(replace(
											replace(
											replace(
											replace(
											replace(
											replace(
											replace(
											replace(.,'&lt;bold&gt;','&lt;strong&gt;'),
											'&lt;/bold&gt;','&lt;/strong&gt;'),
											'&lt;italic&gt;','&lt;em&gt;'),
											'&lt;/italic&gt;','&lt;/em&gt;'),
											'&lt;super&gt;','&lt;sup&gt;'),
											'&lt;/super&gt;','&lt;/sup&gt;'),
											'&lt;underline&gt;',' '),
											'&lt;/underline&gt;',' ')),' ')"
										/>
									</p>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</abstract>

					<fulltext-url>
						<xsl:variable name="accept">
							<xsl:if test="DISS_repository/DISS_acceptance">
								<xsl:value-of select="DISS_repository/DISS_acceptance"/>
							</xsl:if>
							<xsl:if test="not(DISS_repository/DISS_acceptance)">
								<xsl:value-of select="1"/>
							</xsl:if>
						</xsl:variable>
						<xsl:if test="number($accept) != 0">
								<xsl:variable name="pdfpath">
									<xsl:value-of select="DISS_content/DISS_binary"/>
								</xsl:variable>
								<xsl:value-of
									select="concat('https://dl.dropbox.com/u/134495821/', $pdfpath)"
								/>
						</xsl:if>
						
					</fulltext-url>

					<!-- Adds document type -->
					<document-type>
						<xsl:variable name="document">
							<xsl:value-of select="DISS_description/@type"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="starts-with(upper-case($document), 'D')">
								<xsl:value-of>dissertation</xsl:value-of>
							</xsl:when>
							<xsl:when test="starts-with(upper-case($document), 'M')">
								<xsl:value-of>thesis</xsl:value-of>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$document"/>
							</xsl:otherwise>
						</xsl:choose>
					</document-type>

					<!-- Normalizes degree names -->
					<degree_name>
						<xsl:value-of select="DISS_description/DISS_degree"/>
					</degree_name>

					<!-- Normalizes department names -->
					<department>
						<xsl:for-each select="DISS_description/DISS_institution/DISS_inst_contact">
							<xsl:variable name="deptstring">
								<xsl:value-of select="."/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="contains($deptstring, '&amp;')">
									<xsl:value-of select="replace($deptstring, '&amp;', 'and')"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$deptstring"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</department>

					<!-- add abstract format - testing  -->
					<abstract_format>html</abstract_format>

					<fields>

						<!-- add language - add additional spelled out forms of ISO 639-1 codes as needed -->
						<field name="language" type="string">
							<value>
								<xsl:value-of select="DISS_description/DISS_categorization/DISS_language"/>
							</value>
						</field>

						<field name="provenance" type="string">
							<value>Recieved from ProQuest</value>	
						</field>

						<!-- Rights info -->
						<field name="copyright_date" type="string">
							<value>
								<xsl:value-of select="DISS_description/DISS_dates/DISS_comp_date"/>
							</value>
						</field>
						
						<!-- Embargo date -->
						<field name="embargo_date" type="date">
							<value>
								<xsl:variable name="embargo">
									<xsl:value-of select="@embargo_code"/>
								</xsl:variable>
								
								<xsl:variable name="dateString">
									<xsl:value-of select="DISS_repository/DISS_agreement_decision_date"/>
								</xsl:variable>
								<xsl:variable name="fdate" select="concat(substring($dateString, 1, 10), 'T', substring($dateString, 12, 19))"/>
								<xsl:choose>
									<xsl:when test="(number($embargo) = number(1)) and not(string-length($dateString) = 0)">
										<xsl:value-of select="xs:date(xs:dateTime($fdate) + 180*xs:dayTimeDuration('P1D'))"/>
									</xsl:when>
									<xsl:when test="(number($embargo) = number(2)) and not(string-length($dateString) = 0)">
										<xsl:value-of select="xs:date(xs:dateTime($fdate) + 365*xs:dayTimeDuration('P1D'))"/>
									</xsl:when>
									<xsl:when test="(number($embargo) = number(3)) and not(string-length($dateString) = 0)">
										<xsl:value-of select="xs:date(xs:dateTime($fdate) + 730*xs:dayTimeDuration('P1D'))"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="xs:date('2001-01-01')"/>
									</xsl:otherwise>
								</xsl:choose>
							</value>
						</field>

						<!-- Pages numbers (from actual length of PDF, not based on page numbering) -->
						<field name="file_size" type="string">
							<value>
								<xsl:value-of select="concat(DISS_description/@page_count, ' p.')"/>
							</value>
						</field>
						
						<!-- Automatically sets fileformat to pdf -->
						<field name="fileformat" type="string">
							<value>application/pdf</value>
						</field>
						
						<field name="rights_holder" type="string">
							<value>
								<xsl:value-of
										select="DISS_authorship/DISS_author/DISS_name/DISS_fname"/>
									<xsl:text> </xsl:text>
									<xsl:value-of
										select="DISS_authorship/DISS_author/DISS_name/DISS_middle"/>
									<xsl:text> </xsl:text>
									<xsl:value-of
										select="DISS_authorship/DISS_author/DISS_name/DISS_surname"/>
									<xsl:text> </xsl:text>
									<xsl:value-of
										select="DISS_authorship/DISS_author/DISS_name/DISS_suffix"/>
							</value>			
						</field>	

						<!-- Advisors (up to 3 captured) -->
						<xsl:call-template name="advisor"/>
					</fields>
				</document>
			</xsl:for-each>
		</documents>
	</xsl:template>
		<xsl:template match="DISS_description" name="advisor">
		<xsl:if test="DISS_description/DISS_advisor[1]">
			<field name="advisor1" type="string">
				<value>
					<xsl:variable name="fname">
						<xsl:value-of select="DISS_description/DISS_advisor[1]/DISS_name/DISS_fname"
						/>
					</xsl:variable>
					<xsl:variable name="lname">
						<xsl:value-of
							select="DISS_description/DISS_advisor[1]/DISS_name/DISS_surname"/>
					</xsl:variable>

					<xsl:variable name="mname">
						<xsl:value-of
							select="DISS_description/DISS_advisor[1]/DISS_name/DISS_middle"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$mname=''">
							<xsl:value-of select="concat($fname, ' ', $lname)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="minitial">
								<xsl:value-of select="substring($mname,1,1)"/>
							</xsl:variable>
							<xsl:value-of select="concat($fname,' ',$minitial, '. ',$lname)"/>
						</xsl:otherwise>
					</xsl:choose>
				</value>
			</field>

		</xsl:if>
		<xsl:if test="DISS_description/DISS_advisor[2]">
			<field name="advisor2" type="string">
				<value>
					<xsl:variable name="fname">
						<xsl:value-of select="DISS_description/DISS_advisor[2]/DISS_name/DISS_fname"
						/>
					</xsl:variable>
					<xsl:variable name="lname">
						<xsl:value-of
							select="DISS_description/DISS_advisor[2]/DISS_name/DISS_surname"/>
					</xsl:variable>

					<xsl:variable name="mname">
						<xsl:value-of
							select="DISS_description/DISS_advisor[2]/DISS_name/DISS_middle"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$mname=''">
							<xsl:value-of select="concat($fname, ' ', $lname)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="minitial">
								<xsl:value-of select="substring($mname,1,1)"/>
							</xsl:variable>
							<xsl:value-of select="concat($fname,' ',$minitial, '. ',$lname)"/>
						</xsl:otherwise>
					</xsl:choose>
				</value>
			</field>
		</xsl:if>
		<xsl:if test="DISS_description/DISS-advisor[3]">
			<field name="advisor3" type="string">
				<value>
					<xsl:variable name="fname">
						<xsl:value-of select="DISS_description/DISS_advisor[3]/DISS_name/DISS_fname"
						/>
					</xsl:variable>
					<xsl:variable name="lname">
						<xsl:value-of
							select="DISS_description/DISS_advisor[3]/DISS_name/DISS_surname"/>
					</xsl:variable>
					<xsl:variable name="mname">
						<xsl:value-of
							select="DISS_description/DISS_advisor[3]/DISS_name/DISS_middle"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$mname=''">
							<xsl:value-of select="concat($fname, ' ', $lname)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="minitial">
								<xsl:value-of select="substring($mname,1,1)"/>
							</xsl:variable>
							<xsl:value-of select="concat($fname,' ',$minitial, '. ',$lname)"/>
						</xsl:otherwise>
					</xsl:choose>
				</value>
			</field>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
